// Lab1-LFTC.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include<iostream>

using namespace std;


int main()
{
	float r, area, perimeter, pi;
	pi = 3.14;
	cin >> r;
	area = pi * r * r;
	perimeter = 2 * pi * r;
	cout << area << " " << perimeter << " ";
	return 0;
}

