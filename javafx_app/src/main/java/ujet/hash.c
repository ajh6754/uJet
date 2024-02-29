/// Filename: hash.c
///
/// Helps create a hashtable of filenames for pipes
/// @author Albert Jiro Hynes

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>

#define SIZE 100 // have 100 apps at once because, why test the limits? change later.

// define the hashset entry struct
typedef struct Entry 
{
    char * key;
    char * value;
} Entry;

// define the hashtable struct
typedef struct HashTable
{
    Entry * entries[SIZE];
} HashTable;

// hash function
uint32_t hash(char * key)
{
    // initialize the hash
    uint32_t hash = 0;

    // unique hash based on filename
    for(int i = 0; i < strlen(key); i++)
    {
        hash = hash + key[i];
    }

    // return the index where this can be found. TODO deal with collisions later
    return hash % SIZE;
}

// insert into hashtable function
void insert(HashTable * table, char * key, char * value)
{
    // get the index
    uint32_t index = hash(key);
    
    //Entry entry;

    // allocate a new entry
    Entry * entry = malloc(sizeof(Entry));
    entry->key = malloc(strlen(key)+1);
    entry->key = malloc(strlen(value)+1);

    // assign values
    entry->key = key;
    entry->value = value;
    table->entries[index] = entry;
}

// retrieve the value associated with a key from the hash table
char * get(HashTable * table, char * key) 
{
    // get the index
    unsigned int index = hash(key);

    // get the entry 
    Entry * entry = table->entries[index];

    // return the value if it exists.
    if (entry != NULL && strcmp(entry->key, key) == 0) 
    {
        return entry->value;
    }

    // return an error if not found
    return "-1"; 
}

// Function to free the memory allocated for the hash table
void freeHashTable(HashTable * table) 
{
    for (int i = 0; i < SIZE; i++) 
    {
        if (table->entries[i] != NULL) 
        {
            free(table->entries[i]->key);
            free(table->entries[i]->value);
            free(table->entries[i]);
        }
    }
    
    // free the pointers
    // might be an issue here...
    //free(table->entries);
    //free(table);
}

// creates a hashtable
HashTable * createHashTable()
{
    HashTable * table = malloc(sizeof(HashTable));
    for (int i = 0; i < SIZE; i++)
    {
        table->entries[i] = NULL;
    }
    return table;
}