from abc import ABC, abstractmethod

# --- O (Open/Closed) & L (Liskov Substitution) ---
class Account(ABC):   # class name account has be defined
    def __init__(self, account_number, owner):
        self.account_number = account_number
        self.owner = owner
        self.balance = 0.0

    @abstractmethod
    def account_type(self):
        pass

    def deposit(self, amount):     #deposit method
        if amount > 0:
            self.balance += amount
            print(f"Deposited {amount}. New Balance: {self.balance}")
        else:
            print("Deposit must be positive.")

    def withdraw(self, amount):      #withdraw method
        if amount > 0 and amount <= self.balance:
            self.balance -= amount
            print(f"Withdrew {amount}. New Balance: {self.balance}")
        else:
            print("Invalid withdrawal amount.")

    def get_balance(self):         #balance method
        return self.balance


class SavingsAccount(Account):       #class savingsAccounts inherited from accounts class
    def account_type(self):
        return "Savings Account"


class CurrentAccount(Account):        #class currentAccounts inherited from accounts class
    def account_type(self):
        return "Current Account"


# --- S (Single Responsibility) ---
class TransactionService:
    def __init__(self, account: Account):
        self.account = account

    def perform_deposit(self, amount):
        self.account.deposit(amount)

    def perform_withdrawal(self, amount):
        self.account.withdraw(amount)


# --- D (Dependency Inversion) ---
class NotificationService(ABC):
    @abstractmethod
    def notify(self, message):
        pass


class EmailNotification(NotificationService):
    def notify(self, message):
        print(f"[EMAIL]: {message}")


class SMSNotification(NotificationService):
    def notify(self, message):
        print(f"[SMS]: {message}")


# --- I (Interface Segregation) ---
class AccountInfo(ABC):
    @abstractmethod
    def display_info(self):
        pass


class BasicAccountInfo(AccountInfo):
    def __init__(self, account: Account):
        self.account = account

    def display_info(self):
        print(f"Account No: {self.account.account_number}, "
              f"Owner: {self.account.owner}, "
              f"Type: {self.account.account_type()}, "
              f"Balance: {self.account.get_balance()}")


# --- Example Usage ---
if __name__ == "__main__":
    acc1 = SavingsAccount("1001", "Vijay")
    acc2 = CurrentAccount("1002", "Rahul")

    # Transactions
    service1 = TransactionService(acc1)
    service1.perform_deposit(5000)
    service1.perform_withdrawal(1200)

    # Notifications
    notifier = EmailNotification()
    notifier.notify("Transaction completed successfully.")

    # Account Info
    info1 = BasicAccountInfo(acc1)
    info1.display_info()

    info2 = BasicAccountInfo(acc2)
    info2.display_info()
