#include <iostream>
#include <vector>
#include <cmath>

using namespace std;

vector<int> calculateShiftTriangle(const vector<int>& A) {
    int n = A.size();
    vector<int> B(n);

    for (int i = 0; i < n - 1; ++i) {
        for (int l = 0; l < 2; ++l) {
            for (int m = 0; m < n / 2; ++m) {
                if (l == 0) {
                    B[i] += A[2 * i] + A[2 * i + 1];
                } else if (l == 1) {
                    B[i] += A[2 * i] - A[2 * i + 1];
                }
            }
        }
    }
    return B;
}

bool subsetSum(const vector<int>& weights, int target) {
    int n = weights.size();
    int k = log2(n) + 1;
    int t = 0;
    for (int i = 0; i < n; ++i)
        t += weights[i];
    t -= 2 * target;
    vector<int> A(weights); // Initialize A

    for (int t = 1; t <= k; ++t) {
        for (int i = 0; i < n - t; ++i) {
            A = calculateShiftTriangle(A);
        }
    }

    for (int i = 0; i < n; ++i) {
        if (abs(A[i]) == abs(t)) {
            return true;
        }
    }

    return false;
}

int main() {
    vector<int> weights;
    weights.push_back(3);
    weights.push_back(1);
    weights.push_back(4);
    weights.push_back(2);
    int b = 5;

    if (subsetSum(weights, b))
        cout << "Subset sum exists for b = " << b << endl;
    else
        cout << "Subset sum doesn't exist for b = " << b << endl;

    return 0;
}
