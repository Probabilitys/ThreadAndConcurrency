#include <stdio.h> 
#include <stdlib.h> 
#include <unistd.h> 
#include <sys/wait.h>
int main(int argc, char *argv[]) 
{ 
    printf("hello world (my pid is:%d)\n", (int) getpid());
    int rc = fork(); 
    if (rc < 0) 
    { // fork failed // anoher implementation: fprintf(stderr, "fork failed\n"); exit(1); 
        printf("fork unsuccessful");
        return 1;
    } 
    else if (rc == 0) 
    { // child (new process) 
        printf("hello, I am child (pid:%d)\n", (int) getpid()); 
    } 
    else // same as (rc >0)
    { // parent goes down this path (main) 
        // wait (NULL); // Enable this to guarantee that the child will always finish first before the parent
        printf("hello, I am a parent (pid:%d) of the child %d\n", (int) getpid(), rc); 
    } 
    return 0; 
}