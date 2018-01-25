The client connects to a listening socket on the specified server. 
The message sent is in two formats
1. a query is sent by sending a single word, the response received will be either the key saved or the text null
2. a save request is sent by sending a message in the format of "key:value". The server responds by acknowledging that the key was saved.

The server begins by opening a listening socket on the specified port. As it receives requests, it spawns a new thread to handle that request.
Core assignment is done on a round robin fashion per new thread.
