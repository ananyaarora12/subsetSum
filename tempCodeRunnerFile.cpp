#include <iostream>
#include <vector>
#include <algorithm>
#include <numeric> // Include numeric header for accumulate function
#include <climits>

using namespace std;
bool targetFound = false;
vector<int> solution;

void branchAndBoundSubsetSum(const vector<int>& S, int cur, int i, int imax, int& best, int t, vector<int>& currentSet) {
    if (i >= imax || cur > t || targetFound) {
        return;
    }
    int cur1 = cur + S[i];
    int cur2 = cur;

    if (cur1 == t) {
        targetFound = true;
        solution = currentSet;
        solution.push_back(S[i]); // Include current element in the set
        return;
    }

    if (cur1 <= t && cur1 < best) { // Adjusted condition to prioritize subsets closer to the target
        best = cur1;
    }

    if (i + 1 < imax && cur1 <= t && !targetFound) { // Adjusted condition for exploring subsets closer to the target
        currentSet.push_back(S[i]); // Include current element in the set
        branchAndBoundSubsetSum(S, cur1, i + 1, imax, best, t, currentSet);
        currentSet.pop_back(); // Backtrack and remove current element from the set
    }
    if (i + 1 < imax && cur2 <= t && !targetFound) { // Adjusted condition for exploring subsets closer to the target
        branchAndBoundSubsetSum(S, cur2, i + 1, imax, best, t, currentSet);
    }
}
int main() {
    vector<int> S = {2, 3, 7, 8, 10};
    sort(S.rbegin(), S.rend()); // Sort in descending order

    int cur = 0, i = 0, imax = S.size(), best = INT_MAX;
    int t = accumulate(S.begin(), S.end(), 0) / 2; // Target value
    vector<int> currentSet;

    branchAndBoundSubsetSum(S, cur, i, imax, best, t, currentSet);

    if (targetFound) {
        cout << "Exact solution found! Subset with sum equal to target:" << endl;
        for (int num : solution) {
            cout << num << " ";
        }
        cout << endl;
    } else {
        cout << "No exact solution found." << endl;
    }
    return 0;
}