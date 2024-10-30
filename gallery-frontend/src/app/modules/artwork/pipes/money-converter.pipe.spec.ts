import { MoneyConverterPipe } from './money-converter.pipe';

describe('MoneyConverterPipe', () => {
  it('create an instance', () => {
    const pipe = new MoneyConverterPipe();
    expect(pipe).toBeTruthy();
  });
});
