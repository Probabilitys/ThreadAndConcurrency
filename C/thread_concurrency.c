#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <stdbool.h>

// This program creates four threads to search an integer in an array

// define the global array and array size
int arrSize = 16;
int arr[16] = {5, 8, 9, 10, 13, 15, 16, 19,
        20, 22, 26, 27, 35, 50, 70, 99};

// define a new data type called data to three elements of type:
// an integer ponter and two integers
typedef struct data {
    // an integer pointer to hold the array of searching
    int* arr;
    // an integer to hold the number to search
    int search_num;
    // an integer indicating which part of the array to search
    int part;
} data;

// the function used to run by threads
void *arrSearch(void* p) {
    // extract information from the pointer to type data
    data* ptr = (data*) p;
    int i = (*ptr).part;
    int num = (*ptr).search_num;

    // set the start and end index for searching
    // each thread covers four elements
    int start_index = (int) (arrSize * i / 4);  // inclusive
    int end_index = start_index + 4;  // exclusive

    // dynamically declare an int pointer indicating 
    // whether the number has found
    // 1 indicates found, 0 indicates not found
    int* found = (int*) calloc(1, sizeof(int));

    // search the number from start_index to end_index
    for (int i = start_index; i < end_index; i++)
    {
        if (arr[i] == num) { // found the number
            found[0] = 1;  // set to 1 indicating found
            break;  // exit the for loop
        }
    }
    
    pthread_exit(found);
}


int main(void) {

    // read an integer from the user for searching
    int user_num;
    printf("Enter a number to search: \n");
    scanf("%d", &user_num);

    // declare and initialize information for four threads
    data thread_data[4];
    for (int i = 0; i < 4; i++) {
        thread_data[i].arr = arr;
        thread_data[i].search_num = user_num;
        thread_data[i].part = i;
    }

    // create and start four threads using appropriate data
    // each thread will search a quarter of the array
    pthread_t tid[4];
    for (int i = 0; i < 4; i++)
    {
        pthread_create(&tid[i], NULL, arrSearch, &thread_data[i]);
    }
    
    // declare four int pointers to hold the results from four threads
    int* result0;
    int* result1;
    int* result2;
    int* result3;

    // wait the threads to finish
    pthread_join(tid[0], (void**)&result0);
    pthread_join(tid[1], (void**)&result1);
    pthread_join(tid[2], (void**)&result2);
    pthread_join(tid[3], (void**)&result3);
    
    // indicates whether the number is found in the entire array
    // 1 indicates found, 0 indicates not found
    int found_in_arr = *result0 + *result1 + *result2 + *result3;

    // display the final result
    if (found_in_arr == 1)
        printf("The %d is found\n", user_num);
    else
        printf("The %d is not found\n", user_num);

    return 0;
}