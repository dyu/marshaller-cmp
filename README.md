# What is Marshaller-cmp?

Just a simple test for jvm serializers, like [Ignite Binary Mashaller](https://apacheignite.readme.io/docs/binary-marshaller) and [protostuff](http://www.protostuff.io).

# Modules

This maven project has four modules:

* modules/PofObjects which provides the testing data for different serializers, see [example.datahelper.DataHelper.DataType](modules/PofObjects/src/main/java/example/datahelper/DataHelper.java) for more details.
* modules/utils provides helper functions, like time measuring, object's size introspector, and programming command options. You can append '-h' or '--help' for detail options.
* modules/ignite-marshaller, a simple main function get the command options and testing all the data types from module/PofObjects with ignite binary marshaller.
* modules/protostuff-marshaller, a simple main function get the command options and testing all the data types from module/PofObjects with protostuff marshaller.

# How to use it?

Compile the PofObjects and utils modules, and compile the ignite-marshaller and protostuff-marshaller. Run the example.perf.ignite.IgniteMarshaller or example.perf.protostuff.ProtoStuffMarshaller with any parameters you want.

