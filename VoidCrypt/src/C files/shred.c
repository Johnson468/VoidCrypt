
#include<stdio.h>
#include<stdlib.h>

//Function prototypes

//Read a file and print its contents
int main(int argc, char *argv[]) {
	FILE *fptr = fopen(argv[1],"r");
	if(fptr == 0) {
		printf("Error");
		return 0;
	}
	int x;
	while((x=fgetc(fptr)) != EOF) {
		printf("%c",x);
	}
	fclose(fptr);

}
