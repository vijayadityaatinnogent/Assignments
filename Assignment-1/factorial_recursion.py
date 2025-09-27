num = int(input("Enter the number for Factorial- "))
def fact(num: int) -> int:
    if num <= 1:
        return 1
    else:
        return num * fact(num-1)
print(fact(num))