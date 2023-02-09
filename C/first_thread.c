#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>  // Header file for sleep()
#include <pthread.h>

void *myFirstThread(void *vargp) {
    sleep(3);  // keep the execution be slept for 3 secs
    printf("Printing from the Thread \n");
    sleep(5);
    return NULL;  // a thread can have return value
}

int main() 
{
    pthread_t thread_id;
    printf("Calling the Thread ...\n");
    pthread_create(&thread_id, NULL, myFirstThread, NULL);
    pthread_join(thread_id, NULL);  // wait for the thread to finish
    printf("Back from the thread Thread \n");  // the main program resumes its execution from here
    exit(0);
}