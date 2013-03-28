# What is Mango URI?

Mango URI promotes distributed data linking in a multi-cloud cluster. 

# Why do we care?

When managing data at cloud scale, especially between multiple connected clouds, it's not possible to replicate and synchronize every piece of data. In these cases, it's still important to link data between the clouds and be able to resolve the links. The URI is the standard contract by which services can honor a linked request for data.

# How does it work?

Easy, we impose a small set of rules to constructing URIs that make it possible to resolve the services responsible for resolving the URI and returning data based on its contents.

The following is an example of a URI link:
```
any:blob://txt#My%20File.txt
```


Let's pick apart the above URI into meaningful component to better explain how it works. There are three main components:

1. Target system
2. Target service for resolving
3. Parameters


It would suit us well to talk about the URI from the inside out. Let's start with #2 & #3.

## Resolving the "Service Request"
