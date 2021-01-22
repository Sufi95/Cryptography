# This Code Generates the Public and Private key 
import math

def modInverse(a, m):
    a = a % m
    for x in range(1, m):
        if ((a * x) % m == 1):
            return x
    return 1

p = int(input ("P: "))
q = int(input("Q: "))
n = p * q
o = (p-1) * (q-1)
candidates = list()

print("n: ", n)
print("o(n): ", o)

for i in range(1, o):
    if math.gcd(o, i) == 1:
        candidates.append(i)

print ("candidates for e : ", candidates)
e = int(input("Choose value of e: "))
d = modInverse(e, o)
print ("Public Key: ", e)
print ("Private Key: ", d)

