# Hillary
Hillary is a multithreaded, private chat server and chat client that can be run on any LAN or over the internet.  

How to use Hillary:

1 Identify IP address of server (IPConfig for windows, ifconfig for Linux)
2. Run Hillary Chat Server
3. Run Client
4. When prompted by client, input IP address of Hillary chat server
5. Enter unique username

Technology Stack: 

-- Java 8 (secure sockets)
-- Java Swing

Functionality: 

-- Ability to connect to chat server over any network (internet or LAN)
-- Ability to restrict users from having the same username
-- Ability to restrict users from having empty usernames
-- Ability to restrict users from sending null or empty messages


TODOS:

-- user can currently log in with an empty or null username (Serverside Validation)
-- null user message throws NPE
-- Make UI more user friendly
-- Make .exe? 