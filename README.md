# Bcrypt

HomePage: [https://github.com/patrickfav/bcrypt](https://github.com/patrickfav/bcrypt)

## Simple example

```java
String password = "1234";
String bcryptHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());
// $2a$12$US00g/uMhoSBm.HiuieBjeMtoN69SN.GE25fCpldebzkryUyopws6
BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), bcryptHashString);
// result.verified == true
```

## Performance

|             | cost 6  | cost   8 | cost 10  | cost 12   | cost 14   |
|-------------|---------|----------|----------|-----------|-----------|
| favreBcrypt | 3.38 ms | 13.54 ms | 53.91 ms | 216.01 ms | 873.93 ms |

More benchmarks can be found in the [wiki](https://github.com/patrickfav/bcrypt/wiki/Benchmark).