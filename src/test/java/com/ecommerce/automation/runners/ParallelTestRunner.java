package com.ecommerce.automation.runners;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    ChromeTestRunner.class,
    FirefoxTestRunner.class
})
public class ParallelTestRunner {
    // Bu sınıf sadece Chrome ve Firefox test runner'larını birlikte çalıştırır
} 