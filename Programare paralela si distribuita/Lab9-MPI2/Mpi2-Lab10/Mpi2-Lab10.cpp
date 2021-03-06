// Mpi2-Lab10.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <iostream>
#include <mpi.h>
#include <vector>
#include <assert.h>
#include <algorithm>

const int MAX_VALUE = 100;

using namespace std;


void readCoeffs(vector<int>& A, vector<int>& B)
{
	int size, x;
	cerr << "input polynomial size: ";
	cin >> size;
	cerr << "input first polynomial coefficiants: ";

	A.clear();
	for (int i = 0; i < size; i++)
	{
		cin >> x;
		A.push_back(x);
	}

	cerr << "input second polynomial coefficiants: ";

	B.clear();
	for (int i = 0; i < size; i++)
	{
		cin >> x;
		B.push_back(x);
	}
}

void printCoeffs(vector<int>& P)
{
	cerr << "result: ";
	for (auto coeff : P)
		cerr << coeff << " ";
	cerr << "\n";
}

void bruteForce(int* A, int* B, int* R, int n)
{
	for (int i = 0; i < 2 * n; i++)
		R[i] = 0;

	for (int i = 0; i < n; i++)
		for (int j = 0; j < n; j++)
			R[i + j] += A[i] * B[j];
}

void karatsuba(int* A, int* B, int* R, int n)
{
	if (n <= 4)
	{
		bruteForce(A, B, R, n);
		return;
	}

	int i;
	int* Ar = &A[0];               // low-order half of A
	int* Al = &A[n / 2];           // high-order half of A
	int* Br = &B[0];               // low-order half of B
	int* Bl = &B[n / 2];           // high-order half of B
	int* Asum = &R[n * 5];         // sum of A's halves
	int* Bsum = &R[n * 5 + n / 2]; // sum of B's halves
	int* x1 = &R[n * 0];           // Ar*Br's location
	int* x2 = &R[n * 1];           // Al*Bl's location
	int* x3 = &R[n * 2];           // asum*bsum's location

	for (i = 0; i < n / 2; i++)
	{
		Asum[i] = Al[i] + Ar[i];
		Bsum[i] = Bl[i] + Br[i];
	}

	karatsuba(Ar, Br, x1, n / 2);
	karatsuba(Al, Bl, x2, n / 2);
	karatsuba(Asum, Bsum, x3, n / 2);

	for (i = 0; i < n; i++)
		x3[i] = x3[i] - x1[i] - x2[i];
	for (i = 0; i < n; i++)
		R[i + n / 2] += x3[i];
}

void sendWork(vector<int>& A, vector<int>& B, int noProcs)
{
	// cerr << "godfather sends work \n";
	int Asize = (int)A.size();
	for (int i = 1; i < noProcs; i++)
	{
		int left = i*  Asize / noProcs;
		int right = min(Asize, (i + 1)*  Asize / noProcs);
		MPI_Send(&Asize, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
		MPI_Send(&left, 1, MPI_INT, i, 1, MPI_COMM_WORLD);
		MPI_Send(&right, 1, MPI_INT, i, 2, MPI_COMM_WORLD);
		MPI_Send(A.data() + left, right - left, MPI_INT, i, 3, MPI_COMM_WORLD);
		MPI_Send(B.data(), Asize, MPI_INT, i, 4, MPI_COMM_WORLD);
	}
	// cerr << "godfather sent work \n";
}

void doWork(int left, int right, vector<int>& A, vector<int>& B, vector<int>& R)
{
	// cerr << "do work from " << left << " to " << right << "\n";
	karatsuba(A.data(), B.data(), R.data(), (int)A.size());
	// cerr << "work done \n";
}

void collectWork(int n, int noProcs, vector<int>& R)
{
	// cerr << "godfather collects work \n";
	vector<int> aux(2 * n - 1);
	for (int i = 1; i < noProcs; i++)
	{
		MPI_Status _;
		int left = i * n / noProcs;
		int right = min(n, (i + 1) * n / noProcs);
		MPI_Recv(aux.data(), 2 * n - 1, MPI_INT, i, 5, MPI_COMM_WORLD, &_);
		for (int i = 0; i < 2 * n - 1; i++)
			R[i] += aux[i];
	}
	// cerr << "godfather collected work\n";
}

void checkResult(vector<int>& A, vector<int>& B, vector<int>& R)
{
	// cerr << "godfather checks work \n";
	vector<int> CR(A.size() + B.size() - 1, 0);
	for (int i = 0; i < A.size(); i++)
		for (int j = 0; j < B.size(); j++)
			CR[i + j] += A[i] * B[j];

	assert(CR.size() == R.size());
	for (int i = 0; i < CR.size(); i++)
		assert(CR[i] == R[i]);
	// cerr << "godfather checked work \n";
}

void worker(int id)
{
	// cerr << "worker(" << id << ") started \n";
	int n, left, right;
	MPI_Status _;
	MPI_Recv(&n, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &_);
	MPI_Recv(&left, 1, MPI_INT, 0, 1, MPI_COMM_WORLD, &_);
	MPI_Recv(&right, 1, MPI_INT, 0, 2, MPI_COMM_WORLD, &_);
	vector<int> A(n, 0);
	vector<int> B(n, 0);
	MPI_Recv(A.data() + left, right - left, MPI_INT, 0, 3, MPI_COMM_WORLD, &_);
	MPI_Recv(B.data(), n, MPI_INT, 0, 4, MPI_COMM_WORLD, &_);
	vector<int> R(6 * n, 0);
	doWork(left, right, A, B, R);
	MPI_Send(R.data(), 2 * n - 1, MPI_INT, 0, 5, MPI_COMM_WORLD);
	// cerr << "worker(" << id << ") finished \n";
}

int main(int argc, char* argv[])
{
	int id, noProcs;
	vector<int> A, B;

	MPI_Init(0, 0);
	MPI_Comm_size(MPI_COMM_WORLD, &noProcs);
	MPI_Comm_rank(MPI_COMM_WORLD, &id);

	if (id == 0)
	{
		readCoeffs(A, B);
		int n = (int) A.size(), size = n;
		while (n & (n - 1)) // must be a power of 2
		{
			n++;
			A.push_back(0);
			B.push_back(0);
		}
		// cerr << "godfather started \n";
		sendWork(A, B, noProcs);
		int left = 0;
		int right = n / noProcs;
		vector<int> aux(A);
		for (int i = right; i < aux.size(); i++)
			aux[i] = 0;
		vector<int> R(6 * n);
		doWork(left, right, aux, B, R);
		collectWork(n, noProcs, R);
		A.resize(size);
		B.resize(size);
		R.resize(2 * size - 1); // eliminate 0's
		checkResult(A, B, R);
		printCoeffs(R);
		// cerr << "godfather finished \n";
	}
	else
	{
		worker(id);
	}

	MPI_Finalize();
	return 0;
}

