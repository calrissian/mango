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


It would suit us well to talk about the URI from the inside out. Let's start with #2 & #3, which form a component called the "Service Request".

## Resolving the "Service Request"

The service request in the above URI follows the form:
```
service://parameters
```

Ultimately, it is this part of the URI that allows some registered service to fetch the data being requested in the link (if that data still exits). In the URL above, the "blob://" part tells the service resolver to use the registered blob service as its mechanism for returning the data requested. The "txt#My%20File.txt" component is sent to the registered blob service so that it knows specifically what to return. In this case, the blob service knows to return a 'txt' blob with an id of "My File.txt".

## Targeting a remote system

Part #1 of the URI example above represents a target system. Generally, systems connected to a multi-cloud cluster are individually named to identify themselves. This component uses that name to target data on that specific cloud. A target system of "any" means that the first system that has data and responds to a URI request can send the data.

# Example URI Resolver

Now that we've gone over the format of a Mango URI, let's dive into an example URI resolver:
```java
public class EchoStringUriResolver extends BasicObjectUriResolver<String> {

    public EchoStringUriResolver() {}

    @Override
    public String getServiceName() {
      return "string";
    }

    @Override
    public String resolveUri(URI uri, String[] auths) {

      return uri.getRawAuthority();
    }
}
```

It's obvious that this example is oversimplified, but it's good to see how easy it is to construct a URI resolver. Let's register this resolver with the main context so that we can resolve it:
```java
UriResolverContext.getInstance().addResolver(new EchoStringUriResolver());
```

Now, you can resolve any uri's that have "string://" using the following:
```java
URI uri = new URI("string://I%20AM%20A%20URI");
UriResolver resolver = UriResolverContext.getInstance().getResolver(uri);
System.out.println(resolver.resolveUri(uri, null));
```

This will print the following:
```java
I AM A URI
```

# I'm Resolving URIs, now what?

This is where things get good. The Mango URI JMS module contains services to syncrhonously send/receive data over a topic given a URI. 
