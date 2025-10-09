# Java Playground 2025 : Compliance for banking transactions

## Prerequisites

* Maven 3.9+
* Java 21+

## Getting started

* Clone repository
* Make sure you are running Java 21
* Build the code with `mvn prettier:write clean install`
* That's it

## 🧭 Business Need

Simple compliance check for banking transactions, based on client data, country restrictions, and sanction lists.

## 🧠 Application Logic

* Check if the client is in the sanction list
* Check if the destination country is under embargo
* Check if the transaction amount exceeds a threshold
* Return either APPROVED, REJECTED, or REVIEW_REQUIRED
