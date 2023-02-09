#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

int arrSize = 10;

// This program creates two threads that parallelly sum up the values of an array

// a new datatype is created named data that can hold two data elements of types:
// an integer pointer and an integer

// The pthread_create function has only one argument (the 4th and last one) to be
// ultimately passed into the thread function (halfSum), a new datatype(data - struct)
// is defined allowing multiple arguments (arr, thread_num) to pass
typedef struct data{
    int* arr;
    int thread_num;
} data;

// the function below will be run by thread(s)
void* halfSum(void* p) {
    data* ptr = (data*) p;  // p is being typecasted (data *) to be assigned to a pointer of datatype "data"
    int n = (*ptr).thread_num; // dereferencing the value: *ptr, it located what *p locates

    // Declare sum dynamically to return to join:
    // int* thread_sum would have generated segmentation fault, 
    // when more processes attempt to write the same address space;
    int* thread_sum = (int*) calloc(1, sizeof(int)); 
    if (n == 0) {
        for (int i = 0; i < arrSize/2; i++)
            thread_sum[0] = thread_sum[0] + (*ptr).arr[i];
    }
    else {
        for (int i = arrSize/2; i < arrSize; i++)
            thread_sum[0] = thread_sum[0] + (*ptr).arr[i];
    }

    pthread_exit(thread_sum);
}



int main(void) {

    // declare the integer array
    int int_arr[arrSize];

    for (int i = 0; i < arrSize; i++)
        int_arr[i] = i + 1;

    // declare and inilialize arguments for both threads:
    data thread_data[2];
    thread_data[0].thread_num = 0;
    thread_data[0].arr = int_arr;
    thread_data[1].thread_num = 1;
    thread_data[1].arr = int_arr;

    // declare two thread ID to be used by two threads:
    pthread_t tid[2];

    // start both threads that uses same halfSum function but diff args;
    pthread_create(&tid[0], NULL, halfSum, &thread_data[0]);
    pthread_create(&tid[1], NULL, halfSum, &thread_data[1]);

    // declare space for sum
    int* sum0;
    int* sum1;

    // retrieve sum of threads
    pthread_join(tid[0], (void**)&sum0);
    pthread_join(tid[1], (void**)&sum1);

    printf("sum of whole array = %d\n", *sum0 + *sum1);

    return 0;
}