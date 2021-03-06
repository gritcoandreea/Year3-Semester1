#include "stdafx.h"
#include <future>
#include <thread>
#include <iostream>
#include <conio.h>
#include <windows.h>

using namespace std;

const int SUM_SIZE_1 = 10, SUM_SIZE_2 = 10;


void PrintMatrix(int m[][SUM_SIZE_2]);



int main()
{
	int m1[SUM_SIZE_1][SUM_SIZE_2], m2[SUM_SIZE_1][SUM_SIZE_2], resSum[SUM_SIZE_1][SUM_SIZE_2], resMul[SUM_SIZE_1][SUM_SIZE_2];

	for (int i = 0; i < SUM_SIZE_1; i++)
	{
		for (int j = 0; j < SUM_SIZE_2; j++)
		{
			m1[i][j] = 1;
			m2[i][j] = 2;
			resMul[i][j] = 0;
		}
	}

	vector<future<int>> futures;
	for (int i = 0; i < SUM_SIZE_1; i++)
	{
		for (int j = 0; j < SUM_SIZE_2; j++)
		{
			futures.push_back(async(launch::async, [m1, m2, i, j] { return (m1[i][j] + m2[i][j]); }));
		}
	}
	
	for (int i = 0; i < SUM_SIZE_1; i++)
	{
		for (int j = 0; j < SUM_SIZE_2; j++)
		{
			resSum[i][j] = futures[i*SUM_SIZE_1 + j].get();
		}
	}

	vector<future<int>> multFutures;
	for (int i = 0; i < SUM_SIZE_1; i++)
	{
		for (int j = 0; j < SUM_SIZE_2; j++)
		{
			for (int k = 0; k < SUM_SIZE_1; k++)
			{
				multFutures.push_back(async(launch::async, [m1, m2, i, j, k] { return (m1[i][k] * m2[k][j]); }));
			}

		}
	}
	for (int i = 0; i < SUM_SIZE_1; i++)
	{
		for (int j = 0; j < SUM_SIZE_2; j++)
		{
			for (int k = 0; k < SUM_SIZE_1; k++)
			{
				int temp = i*SUM_SIZE_2 + j * SUM_SIZE_1 + k;
				resMul[i][j] += multFutures[i*SUM_SIZE_2 * SUM_SIZE_1 + j * SUM_SIZE_1 + k].get();
			}

		}
	}

	cout << "SUM:"<<endl;
	PrintMatrix(resSum);
	cout << "----------------"<< endl;

	cout << "MUL:"<<endl;
	PrintMatrix(resMul);
	
	return 0;



}

void PrintMatrix(int m[][SUM_SIZE_2]) {

	for (int i = 0; i < SUM_SIZE_1; i++)
	{
		for (int j = 0; j < SUM_SIZE_2; j++)
		{
			cout << m[i][j] << " ";
		}
		cout << endl;
	}

}