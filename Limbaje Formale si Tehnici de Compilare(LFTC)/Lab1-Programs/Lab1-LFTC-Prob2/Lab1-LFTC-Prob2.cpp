// Lab1-LFTC-Prob2.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <iostream>
using namespace std;

int main()
{
	int a, b;
	cin >> a;
	cin >> b;

	while (a != b) {
		if (a > b) {
			a = a - b;
		}
		else
		{
			b = b - a;
		}
	}
	cout << a;
	return 0;
}

