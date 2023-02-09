#define NUMBER_OF_PROCESSES 5
#define NUMBER_OF_RESOURCES 4

// a function to compare two vectors
int compare_vector(int length, int v1[], int v2[]) {
    for (int i = 0; i < length; i++) {
        if (v1[i] < v2[i])
            return 0;
    }
    return 1;
}

// check if the system is in safe state
int check_safe(int available[], int allocation[][NUMBER_OF_RESOURCES], 
                int need[][NUMBER_OF_RESOURCES]) {

    // initialize a work vector
    int work[NUMBER_OF_RESOURCES];
    for (int i = 0; i< NUMBER_OF_RESOURCES; i++)
        work[i] = available[i];

    // initialize a finish vector
    int finish[NUMBER_OF_PROCESSES] = {0, 0, 0, 0, 0};
    int safe = 1;  // 0 indicates unsafe, 1 indicates safe

    // find an i such that both:
    // finish[i] = 1
    // need i < work
    for (int check = 0; check < NUMBER_OF_PROCESSES; check++) {
        for (int i = 0; i < NUMBER_OF_PROCESSES; i++) {
            if (finish[i] == 0 && 
                    compare_vector(NUMBER_OF_RESOURCES, work, need[i])) { // found i
                for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
                    work[j] = work[j] + allocation[i][j];
                    finish[i] = 1;
                }
            }
        }
    }

    // check if finish[i] = 1 for every i
    // if true(1) for all i, the system is in a safe state
    for (int i = 0; i < NUMBER_OF_PROCESSES; i++) {
        if (finish[i] != 1) {
            safe = 0;
            break;
        }
    }

    return safe;
}

// resource-request algorithm
void resource_request(int request_process, int request[], int available[], 
                    int allocation[][NUMBER_OF_RESOURCES], int need[][NUMBER_OF_RESOURCES]) {

    // if request < need i
    if (compare_vector(NUMBER_OF_RESOURCES, need[request_process], request)) {

        // if request < available
        if (compare_vector(NUMBER_OF_RESOURCES, available, request)) {
            // available = availablle - request
            for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
                available[i] = available[i] - request[i];
            }
            // allocation = allocation + request
            for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
                allocation[request_process][i] = allocation[request_process][i] + request[i];
            }
            // need = need - request
            for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
                need[request_process][i] = need[request_process][i] - request[i];
            }
            
            if (check_safe(available, allocation, need)) // if in safe state now, request is granted
                printf("request granted\n");
            else  // otherwise the process must wait, current state is restored
                printf("process must wait\n");
        }
        else {  // resources are not available
            printf("resources are not available\n");
        }
    } 
    else {  // exceed the maximum claim
        printf("exceed the maximum claim\n");
    }
}

int main(int argc, char *argv[]) {

    // number of available resources
    int available[NUMBER_OF_RESOURCES] = {1, 5, 2, 0};

    // the maximum demand of each process
    int max[NUMBER_OF_PROCESSES][NUMBER_OF_RESOURCES] = {
        {0, 0, 1, 2},
        {1, 7, 5, 0},
        {2, 3, 5, 6},
        {0, 6, 5, 2},
        {0, 6, 5, 6}
    };

    // the current allocation of each process
    int allocation[NUMBER_OF_PROCESSES][NUMBER_OF_RESOURCES] = {
        {0, 0, 1, 2},
        {1, 0, 0, 0},
        {1, 3, 5, 4},
        {0, 6, 3, 2},
        {0, 0, 1, 4}
    };

    // the current need of each process
    int need[NUMBER_OF_PROCESSES][NUMBER_OF_RESOURCES];

    // initialize the need matrix
    for (int i = 0; i < NUMBER_OF_PROCESSES; i++) {
        for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
            need[i][j] = max[i][j] - allocation[i][j];
        }
    }
    
    // test different cases of requesting
    // pretend each case is independent
    int request_process = 0;

    int request_0[NUMBER_OF_RESOURCES] = {0, 0, 0, 0};
    printf("process 0 request [0, 0, 0, 0]:\n");
    resource_request(request_process, request_0, available, allocation, need);

    request_process = 1;
    int request_1[NUMBER_OF_RESOURCES] = {1, 0, 0, 1};
    printf("process 1 request [1, 0, 0, 1]:\n");
    resource_request(request_process, request_1, available, allocation, need);

    request_process = 2;
    int request_2[NUMBER_OF_RESOURCES] = {1, 1, 1, 1};
    printf("process 2 request [1, 1, 1, 1]:\n");
    resource_request(request_process, request_2, available, allocation, need);

    request_process = 3;
    int request_3[NUMBER_OF_RESOURCES] = {0, 6, 2, };
    printf("process 3 request [0, 6, 2, 1]:\n");
    resource_request(request_process, request_3, available, allocation, need);

    request_process = 4;
    int request_4[NUMBER_OF_RESOURCES] = {0, 0, 4, 0};
    printf("process 4 request [0, 0, 4, 0]:\n");
    resource_request(request_process, request_4, available, allocation, need);

    return 0;
}