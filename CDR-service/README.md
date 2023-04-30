## Call Data record

----------

Сервис, который генерирует cdr файл с информацией о звонках за указанный месяц и создает новых абонентов.

Количество генерируемых данных указано в application.properties.

### CDR Generator Tests

Сервис сопровожден юнит-тестами следующих классов:

1. [BalanceGenerator Tests](src/test/java/ru/nexign/cdr/BalanceGeneratorTests.java):
   - `testCanConstructDefault()` - класс может быть сконструирован со значениями по умолчанию.
   - `testCanConstructAndSetField()` - конструирование и инициализация приватных полей с помощью `ReflectionTestUtils.setField()`
   - `testCanGenerateBalance()` - метод генерации баланса может быть вызван и отработать без исключений.
   - `testGeneratedBalanceWithinProperBoundaries()` - сгенерированный баланс находится в заданных границах.
   - `testGeneratedValuesAreDifferent()` - возвращаемые значения различны.
3. [TariffGenerator Tests](src/test/java/ru/nexign/cdr/TariffGeneratorTests.java)
   - `testReturnValidTariffs()` - сгененированные тарифы имеют одно из следующих значений: "03", "06", "11".
3. [PhoneNumberGenerator Tests](src/test/java/ru/nexign/cdr/PhoneNumberGeneratorTests.java)
   - `tesReturnValidNumbers()` - сгенерерированного номера проверяются на соответствие заданному стандарту (79xxxxxxxxx).
4. [CallTimeGenerator Tests](src/test/java/ru/nexign/cdr/CallTimeGeneratorTests.java)
   - `testCTGDontThrowOnValidValues(int month, int year)` - генерация времени звонка не падает с валидными значениями.
   - `testCTGThrowsOnInvalidValues(int month, int year)` - на невалидных данных должно броситься ожидаемое исключение `IllegalArgumentException`.
   - `testCTGFormat(int month, int year)` - что сгенерированное время соответствует заданному формату `yyyyMMddHHmmss`.
5. [CdrGenerator Tests](src/test/java/ru/nexign/cdr/CdrGeneratorTests.java)
   - `cleanAfterTest()` - функция, которая удаляет сгенерированные файлы после каждого теста.
   - `testCanConstructDefault()` - класс может быть сконструирован со значениями по умолчанию.
   - `testCanConstructAndSetField()` - конструирование и инициализация приватных полей с помощью `ReflectionTestUtils.setField()`
   - `testCanCallGenerateCdrFile()` - метод создания `CDR` может быть вызван без и отрабатоть без исключений.
   - `testGeneratedFileExists()` - сгенерированный файл существует.
   - `testGeneratedFileHasCorrectStructure()` - структура файла соответствует заданному формату: <tariff>,<phone_number>,<start_time>,<end_time>.
