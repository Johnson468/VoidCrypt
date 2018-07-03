# VoidCrypt
Version: 1.0

[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/johnson468/voidcrypt/issues) 


A Java based desktop app that allows users to encrypt/decrypt their files using AES encryption against a 64 byte key generated from
running a SHA256 hashing algorithm on a password.

Work in progress.

Upon selecting a file, you will have the following options:

# Encrypt
Selecting this will prompt you to enter a password of 8 or more characters, and verify it. You <b><i><u>MUST</u></i></b> remember this password, as there is no way to retrieve it.
The encryption algorithm will run 1000 cycles and display a message when finished.
The application will log a hashed name of the file as well as the hashed password with a salt in C:\VoidCryt\sums.info. 
You can only select encrypt on a file once before decrypting it.
<b>Please note, even though it only lets you select encrypt once, the encryption runs 1000 times</b>

# Decrypt
Selecting this option will prompt you for the password for the file. 
If you get it wrong it will tell you and you will have to try again.
If you enter the correct password, it will decrypt the file and display a message when it's finished.
Once a file is decrypted, you can encrypt it again.

# Shred 
Selecting this option will run the encryption 1000 times with a SecureRandom key generation each time.
After this, the file will be deleted.
In the future this will be done at the byte level.
