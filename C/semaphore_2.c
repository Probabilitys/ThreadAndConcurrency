#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <semaphore.h>  // semaphore header file

#define THREAD_SUM 4

sem_t semaphore;  // semaphore datatype

void* routine(void* args) {
    sem_wait(&semaphore);  // wait() or P()
    sleep(1);
    printf("Hello from thread %d\n", *(int*)args);
    sem_post(&semaphore);  // signal() or V()
    free(args);
}

int main( ) {
    printf("Start\n");
    pthread_t th[THREAD_SUM];
    sem_init(&semaphore, 0, 4);
    int i;
    for (i=0; i<THREAD_SUM; i++) {
        int* a = malloc(sizeof(int));
        *a = i;
        if (pthread_create(&th[i], NULL, &routine, a) != 0) {
            perror("Failed to created thread.");
        }
    }

    for (i=0; i<THREAD_SUM; i++) {
        if (pthread_join(th[i], NULL) != 0) {
            perror("Failed to join thread.");
        }
    }

    sem_destroy(&semaphore);  // deallocation
    return 0;
}