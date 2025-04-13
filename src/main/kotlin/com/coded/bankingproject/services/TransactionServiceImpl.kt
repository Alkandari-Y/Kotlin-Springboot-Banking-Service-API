package com.coded.bankingproject.services

import com.coded.bankingproject.accounts.dtos.TransactionResultDto
import com.coded.bankingproject.accounts.dtos.TransferCreateRequestDto
import com.coded.bankingproject.accounts.exceptions.AccountNotFoundException
import com.coded.bankingproject.accounts.exceptions.IllegalTransferException
import com.coded.bankingproject.accounts.exceptions.InsufficientFundsException
import com.coded.bankingproject.domain.entities.TransactionEntity
import com.coded.bankingproject.repository.AccountRepository
import com.coded.bankingproject.repository.TransactionRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TransactionServiceImpl(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
): TransactionService {

    @Transactional
    override fun transfer(newTransaction: TransferCreateRequestDto): TransactionResultDto {
        if (newTransaction.sourceAccountNumber == newTransaction.destinationAccountNumber) {
            throw IllegalTransferException("Cannot transfer to the same account.")
        }

        val sourceAccount = accountRepository.findByAccountNumber(newTransaction.sourceAccountNumber)
        val destinationAccount = accountRepository.findByAccountNumber(newTransaction.destinationAccountNumber)

        if (sourceAccount == null || destinationAccount == null) {
            throw AccountNotFoundException("One or both accounts not found.")
        }

        if (sourceAccount.isActive.not() || destinationAccount.isActive.not()) {
            throw IllegalTransferException("Cannot transfer with inactive account.")
        }

        val newSourceBalance = sourceAccount.balance - newTransaction.amount
        val newDestinationBalance = destinationAccount.balance + newTransaction.amount

        if (newSourceBalance < BigDecimal.ZERO) {
            throw InsufficientFundsException("Transfer would result in a negative balance.")
        }

        val transaction = transactionRepository.save(
            TransactionEntity(
                sourceAccount=sourceAccount,
                destinationAccount=destinationAccount,
                amount = newTransaction.amount,
            )
        )

        val updatedSourceAccount = accountRepository.save(
            sourceAccount.copy(balance = newSourceBalance)
        )
        val updatedDestinationAccount = accountRepository.save(
            destinationAccount.copy(balance = newDestinationBalance)
        )
        return TransactionResultDto(
            sourceAccount = updatedSourceAccount,
            destinationAccount = updatedDestinationAccount,
            transaction = transaction
        )
    }
}