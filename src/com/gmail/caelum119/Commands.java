package com.gmail.caelum119;

/**
 *
 */
public class Commands{
  private Cbot cbot;
  public Commands(Cbot cbot){
    this.cbot = cbot;
  }
  @CommandSettings(name = "test")
  public void test(String repeat){
    cbot.sendMessage(cbot.channel, repeat + "ddd");
  }
}
