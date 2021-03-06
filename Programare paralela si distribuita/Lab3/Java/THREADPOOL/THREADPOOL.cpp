#include "stdafx.h"
#include <iostream>
#include "ThreadPool.h"
#include <fstream>
#include <vector>

using namespace std;

const int TP = 5;
const int T = 100;

int a[1005][1005], b[1005][1005], res[1005][1005], n, m;

void load() {
	memset(res, 0, sizeof(res));
	n = 10;
	m = 10;
	for (int i = 0; i < n; ++i) {
		for (int j = 0; j < m; ++j) {
			 a[i][j] = i * j;
		}
	}
	for (int i = 0; i < n; ++i) {
		for (int j = 0; j < m; ++j) {
			b[i][j] = i*j;
		}
	}
}

void mult() {
	ThreadPool pool(5);
	vector <future<int>> f;
	for (int i = 0; i < min(n, T); ++i) {
		f.push_back(pool.enqueue([](int line) {
			for (int i = line; i < n; i += T) {
				for (int j = 0; j < n; ++j) {
					for (int l = 0; l < n; ++l) {
						res[i][j] += a[i][j] * b[j][l];
					}
				}
			}
			return line;
		}, i));
	}
	for (int i = 0; i < min(n, T); ++i) {
		cerr << f[i].get() << '\n';
	}
	for (int i = 0; i < n; ++i) {
		for (int j = 0; j < m; ++j) {
			cerr << res[i][j] << ' ';
		}
		cerr << '\n';
	}
}

void add() {
	ThreadPool pool(5);
	vector <future<int>> f;
	for (int i = 0; i < min(n, T); ++i) {
		f.push_back(pool.enqueue([](int line) {
			for (int i = line; i < n; i += T) {
				for (int j = 0; j < m; ++j) {
					res[i][j] = a[i][j] + b[i][j];
				}
			}
			return line;
		}, i));
	}
	for (int i = 0; i < min(n, T); ++i) {
		cerr << f[i].get() << '\n';
	}
	for (int i = 0; i < n; ++i) {
		for (int j = 0; j < m; ++j) {
			cerr << res[i][j] << ' ';
		}
		cerr << '\n';
	}
}

int main() {
	load();
	add();
	load();
	mult();
	return 0;
}
