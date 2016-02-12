package mapEditor.application.main_part.app_utils.threads;

import mapEditor.application.main_part.app_utils.constants.AppConstants;
import mapEditor.application.repo.SystemParameters;

/**
 *
 * Created by razvanolar on 12.02.2016.
 */
public class MessageListener implements Runnable {

  private MessageHandler handler = new MessageHandler();

  @Override
  public void run() {
    System.out.println("> " + AppConstants.APP_LISTENER_THREAD_NAME + " start listening for events.");
    while (true) {
      try {
        synchronized (SystemParameters.MESSAGE_KEY) {
          SystemParameters.MESSAGE_KEY.wait();
          System.out.println("A new message was received : " + SystemParameters.MESSAGE_KEY.getMessageType().name());
        }
        if (SystemParameters.MESSAGE_KEY.getMessageType() != null)
          handler.handleMessage(SystemParameters.MESSAGE_KEY.getMessageType());
      } catch (InterruptedException ex) {
        System.out.println("*** " + AppConstants.APP_LISTENER_THREAD_NAME + " was interrupted. The loop will be closed");
        break;
      } catch (Exception ex) {
        ex.printStackTrace();
        System.out.println("*** " + AppConstants.APP_LISTENER_THREAD_NAME + " encountered an error");
      }
    }
  }
}
