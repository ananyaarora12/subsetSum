#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

int binarySearch(const vector<int>& e, int c, int j, int n) {
    int low = j;
    int high = n;
    
    while (low <= high) {
        int mid = low + (high - low) / 2;
        
        if (c >= e[mid]) {
            if (c == e[mid]) {
                return 1;  // Element found
            }
            low = mid + 1;
        } else {
            high = mid - 1;
        }
    }
    return -1;  // Element not found
}

void findSubset(const vector<int>& e, int X) {
    int n = e.size();
    vector<int> s;

    for (int i = 0; i < n - 1; i++) {
        int sum = 0;

        for (int j = i; j < n - 1; j++) {
            sum += e[j];
            int c = X - sum;
            
            if (c > e[j]) {
                int flag = binarySearch(e, c, j + 1, n - 1);
                
                if (flag == 1) {
                    cout << "Subset found with target sum X\n";
                    for (int k = i, l = 0; k <= j; k++, l++) {
                        s.push_back(e[k]);
                    }
                    s.push_back(c);
                    cout << "Subset: ";
                    for (int num : s) {
                        cout << num << " ";
                    }
                    cout << endl;
                    return;
                }
            } else {
                break;
            }
        }
    }

    cout << "No subset found with target sum X" << endl;
}

int main() {
    vector<int> e;
    e.push_back(2);
    e.push_back(3);
    e.push_back(6);
    e.push_back(7);
    e.push_back(10);
    int X = 17;

    findSubset(e, X);

    return 0;
}
