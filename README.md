# Tradaing Desk

## Introduction
Replicant, from an empty directoryu

Replicant turns plain Clojure data into a web page, and turns new data into just the changes that page needs. 

## The nain idea

The whole user interface is a function of state. You write one ordinary Clojure function. It takes a map describing everything the app knows and returns a description of the entire page as data. Whenever the state changes, you call the function again and hand the new description to Replicant.

Replicant remembers the previous description, compares it to the new one, and makes only the DOM changes needed to get from one to the other. That comparing step is called reconciliation, and it is the whole job of the library.

> The consequence: your view code has no state, no subscriptions and no side effects. It is a pure function you can call in a REPL and inspect like any other data. That is the part that makes Replicant pleasant.
