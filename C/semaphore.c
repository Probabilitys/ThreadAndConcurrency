#define _GNU_SOURCE

#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <semaphore.h>  // semaphore header file

#define TOTAL_PERSON_THREAD 15  // 15 players

sem_t semaphore;  // semaphore datatype

// a play function to enter, play and exit the card game
void* play(void* play_time) {
    // display the players' current status
    printf("Person %d is waiting to play...\n", (int)gettid());
    sem_wait(&semaphore);  // entry section
    printf("Person %d logged in for %d seconds.\n", (int)gettid(), *(int*)play_time);  // critical section
    sleep(*(int*)play_time);
    printf("Person %d logged out.\n", (int)gettid());
    sem_post(&semaphore);  // exit section
    free(play_time);  // remainder section
}

int main(int argc, char *argv[]) {
    srand(time(NULL));  // initialize the random generator
    pthread_t th[TOTAL_PERSON_THREAD];
    sem_init(&semaphore, 0, 4);  // initialize the semaphore and at most 4 players can play at a time
    int i;
    // create a thread to each person
    for (i=0; i<TOTAL_PERSON_THREAD; i++) {
        int* t = malloc(sizeof(int));
        *t = rand() % 3 + 1;  // generate a random play time between 1 to 3 seconds
        if (pthread_create(&th[i], NULL, &play, t) != 0) {
            perror("Failed to created thread.");
        }
    }

    // all players try to log in at the same time
    for (i=0; i<TOTAL_PERSON_THREAD; i++) {
        if (pthread_join(th[i], NULL) != 0) {
            perror("Failed to join thread.");
        }
    }

    sem_destroy(&semaphore);  // deallocation
    return 0;
}