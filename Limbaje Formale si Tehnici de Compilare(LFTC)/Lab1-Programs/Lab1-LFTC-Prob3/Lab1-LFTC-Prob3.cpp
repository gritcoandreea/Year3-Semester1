// Lab1-LFTC-Prob3.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include  <iostream>
using namespace std;

int main()
{
	int n;
	float number, sum;
	sum = 0;
	cin >> n;
	while (n > 0)
	{
		cin >> number;
		sum = sum + number;
		n = n - 1;
	}
	cout << sum;

	return 0;
}