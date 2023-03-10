#include <stdio.h> 
#include <stdlib.h> 
#include <unistd.h> 
#include <string.h> 
#include <sys/wait.h> 
int main(int argc, char *argv[]) 
{ printf("hello world (pid:%d)\n", (int) getpid()); int rc = fork(); 
if (rc < 0) 
{ // fork failed; exit 
fprintf(stderr, "fork failed\n"); exit(1);
} 
else if (rc == 0) 
{ // child (new process) 
printf("Hello, I am child (pid:%d)\n", (int) getpid()); 
printf ("I am replaced by the WC word count program and will be executing it now ...\n");
printf ("(# Lines) (# Words) (# Bytes)  (File Name)\n");
char *myargs[3]; 
myargs[0] = strdup("wc"); // program: "wc" (word count) 
myargs[1] = strdup("process_exec.c"); // argument: file to count 
myargs[2] = NULL; // marks end of array 
execvp(myargs[0], myargs); // runs word count printf("this shouldn’t print out"); 
} 
else 
{ // parent goes down this path (main) 
int rc_wait = wait(NULL); 
printf("\nHello, I am parent of %d (child PID as I get from wait:%d) (my pid:%d)\n", rc, rc_wait, (int) getpid()); 
} 
return 0; 
}