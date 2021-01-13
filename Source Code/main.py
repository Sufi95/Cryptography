from cryptography.hazmat.primitives import hashes
from Cryptodome.Cipher import DES, PKCS1_OAEP, AES
from Cryptodome.PublicKey import RSA
from Cryptodome.Random import get_random_bytes
import time
import os
import hashlib

path1="./Files/10MB.pdf"
path2="./Files/500MB.mp4"
path3="./Files/1GB.mp4"


def mySHA512(file_name):
    FileHasher = hashlib.sha3_512()
    with open(file_name, 'rb') as afile:
        buf = afile.read()
        startTime = time.time()
        FileHasher.update(buf)
        exec_time = time.time() - startTime
    print("\nAlgorithm: SHA512")
    print ("File Size: "+ str(((os.path.getsize(file_name) / 1000 ) / 1000) )+" MB")
    print ("Hash Value: "+ FileHasher.hexdigest())
    print ("Exectuion Time: "+ str(exec_time))


def myRSA (file_name):
    key = RSA.generate(2048)
    private_key = key.export_key()
    file_out = open("private.pem", "wb")
    file_out.write(private_key)
    file_out.close()

    public_key = key.publickey().export_key()
    file_out = open("receiver.pem", "wb")
    file_out.write(public_key)
    file_out.close()

    MyFile =open(file_name, "rb")
    data = MyFile.read()
    file_out = open("encrypted_data.bin", "wb")

    recipient_key = RSA.import_key(open("receiver.pem").read())
    session_key = get_random_bytes(16)

    # Encrypt the session key with the public RSA key
    enc_time =time.time()
    cipher_rsa = PKCS1_OAEP.new(recipient_key)
    enc_session_key = cipher_rsa.encrypt(session_key)

    # Encrypt the data with the AES session key
    cipher_aes = AES.new(session_key, AES.MODE_EAX)
    ciphertext, tag = cipher_aes.encrypt_and_digest(data)
    enc_exec_time = time.time() - enc_time

    [file_out.write(x) for x in (enc_session_key, cipher_aes.nonce, tag, ciphertext)]
    file_out.close()

    file_in = open("encrypted_data.bin", "rb")

    private_key = RSA.import_key(open("private.pem").read())

    enc_session_key, nonce, tag, ciphertext = \
        [file_in.read(x) for x in (private_key.size_in_bytes(), 16, 16, -1)]

    dec_time =time.time()

    # Decrypt the session key with the private RSA key
    cipher_rsa = PKCS1_OAEP.new(private_key)
    session_key = cipher_rsa.decrypt(enc_session_key)

    # Decrypt the data with the AES session key
    cipher_aes = AES.new(session_key, AES.MODE_EAX, nonce)
    data = cipher_aes.decrypt_and_verify(ciphertext, tag)
    dec_exec_time  = time.time() - dec_time
    print("\nAlgorithm: RSA")
    print("File Size: " + str(((os.path.getsize(file_name) / 1000) / 1000)) + " MB")
    print("Encryption Time: " + str(enc_exec_time))
    print("Decryption Time: " + str(dec_exec_time))


myRSA(path1) # encrypt 10MB file
myRSA(path2) # encrypt 500MB file
myRSA(path3) # encrypt 1000MB file

mySHA512(path1) # Hash 10MB file
mySHA512(path2) # Hash 500MB file
mySHA512(path3) # Hash 1000MB file

