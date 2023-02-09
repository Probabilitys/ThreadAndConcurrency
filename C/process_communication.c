#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>

int main(int argc, char *argv[]) 
{
    int GP[] = {8, 2, 6, 12, 8, 3};  // array of Grade Points
    int credit_hrs[] = {4, 2, 3, 3, 2, 3};  // array of credit hours

    int port[2];  // array for the pipe
    pid_t pid;

    if (pipe(port) < 0)  // pipe failed
    {
        printf("pipe error");
        return 1;
    }

    pid = fork();  // create a child process
    if (pid < 0) {  // fork failed
        printf("fork error");
        return 2;
    }

    if (pid > 0)  // parent(main) process
    {
        double GPA;
        int total_gp = 0;
        int length = sizeof(GP) / sizeof(int);
        for (int i = 0; i < length; i++)  // compute the sum of grade points
        {
            total_gp += GP[i];
        }

        wait(NULL);  // wait the child process to finish
        printf("\n--------------Main Process------------\n");

        // read the sum of credit hours from the child process
        int total_credits;
        close(port[1]);
        read(port[0], &total_credits, sizeof(int));
        close(port[0]);

        // compute GPA
        GPA = (double) total_gp / (double) total_credits;

        char name[30];
        printf("Enter student name: ");
        fgets(name, 30, stdin);
        name[strlen(name)-1] = '\0';  // eliminate the ending Enter in the string
        
        printf("The student %s has total grade points %d, total credit hours %d, and GPA %f.\n",
            name, total_gp, total_credits, GPA);
        // print out the process information
        printf("This is the main process (pid: %d) of child process (pid: %d).\n",
            (int) getpid(), pid);

        printf("\nDone.\n");
    }
    else if (pid == 0)  // child process
    {
        int total_credit_hrs = 0;
        int length  = sizeof(credit_hrs) / sizeof(int);
        for (int i = 0; i < length; i++)  // compute the sum of credit hours
        {
            total_credit_hrs += credit_hrs[i];
        }

        // write the sum of credit hours to the pipe
        close(port[0]);
        write(port[1], &total_credit_hrs, sizeof(int));
        close(port[1]);

        printf("--------------Child Process------------\n");
        printf("The total credit hours computed by child process (pid: %d) is %d\n", 
            (int) getpid(), total_credit_hrs);
        // print out the process information
        printf("This is the child process (pid: %d) of parent process (pid: %d).\n", 
            (int) getpid(), (int) getppid());

        return 0;  // child process terminates
    }

    return 0;

}