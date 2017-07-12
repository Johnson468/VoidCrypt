#include<stdio.h>
#include<stdlib.h>
//function prototypes

int size;
 //Read a file and print its contents
 int main(int argc, char *argv[]) {

	size = sizeOfFile(argv[1]);
	printf("%d\n", size);
 	FILE *fptr = fopen(argv[1],"r");
 	overwriteBytes(argv[1], size);
 	if(fptr == 0) {
 		printf("Error");
 		return 0;
 	}

 	int x;
 	while((x=fgetc(fptr)) != EOF) {
 		printf("%c",x);
 	}
 	//getAddress(*fptr);

 	fclose(fptr);

  }


void overwriteBytes(char *file, int bytes) {
	FILE *f = fopen(file,"r");
	char str[100];
	*memset(f, 0, size);
	fclose(f);

}
//char getAddress(FILE file) {
//	printf("%p",&file);
//	return &file;
//}

int sizeOfFile(char *file) {
	FILE *f = fopen(file,"r");
	fseek(f, 0, SEEK_END);
	unsigned long len = (unsigned long)ftell(f);
	fclose(f);
	return len;
}

