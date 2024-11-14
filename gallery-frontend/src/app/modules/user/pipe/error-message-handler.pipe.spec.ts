import { ErrorMessageHandlerPipe } from './error-message-handler.pipe';

describe('ErrorMessageHandlerPipe', () => {
  it('create an instance', () => {
    const pipe = new ErrorMessageHandlerPipe();
    expect(pipe).toBeTruthy();
  });
});
