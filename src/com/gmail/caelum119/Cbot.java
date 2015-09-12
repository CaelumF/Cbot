package com.gmail.caelum119;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class Cbot extends PircBot{
  //Allows users to do commands no matter how long they've been connected, also allows anyone to do operator commands.
  private boolean debugMode;
  private String activator = "cb,,";


  private HashMap<String, Command> commands = new HashMap<String, Command>();

  private static HashMap<String, User> connectedUsers = new HashMap<String, User>();
  private static HashMap<String, User> disconnectedUsers = new HashMap<String, User>();

  public String channel;
  String[] operators = new String[]{"caelum19"};

  public Cbot(){
    this.setName("Cbot3");
    addCommandListener(new Commands(this));

  }

  @Override protected void onMessage(String channel, String sender, String login, String hostname, String message){
    super.onMessage(channel, sender, login, hostname, message);

    boolean sentByOperator = false;
    for(String s : operators){
      if(s.equals(sender)){
        sentByOperator = true;
        break;
      }
    }
    if(message.startsWith(activator) && message.split(" ").length > 0)
    {
      String commandName = message.split(" ")[1];
      Command command = commands.get(commandName);

      if(command != null){
        Type[] types = determineParameterTypes(paramterise(message, 2));
        //Check if types match Command method types.
        for(int i = 0; i < types.length; i++){
          if(!types[i].equals(command.method.getGenericParameterTypes()[i])){
            sendMessage(channel, "Parameter " + i + " does not match required type: " + types[i].toString());
            return;
          }
          try{
            command.method.invoke(this, convertTypes(paramterise(message, 2)));
          }catch(IllegalAccessException e){
            e.printStackTrace();
          }catch(InvocationTargetException e){
            e.printStackTrace();
          }
        }

      }
      else{
        sendMessage(channel, "Command " + commandName + " not found. ");
      }
    }

    if(message.equalsIgnoreCase("go away Cbot") && sender.equalsIgnoreCase("caelum19")){
      disconnect();
    }
  }


  @Override protected void onJoin(String channel, String sender, String login, String hostname){
    super.onJoin(channel, sender, login, hostname);

    //This should always the case, but you never know.
    if(connectedUsers.get(sender) == null){

      //Move disconnected user to connected user if they're already in the system.
      if(disconnectedUsers.get(sender) != null){
        connectedUsers.put(sender, disconnectedUsers.get(sender));
        disconnectedUsers.remove(sender);
        return;
      }
      //Add them to system.
      connectedUsers.put(sender, new User(sender));
    }
  }

  @Override protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason){
    super.onQuit(sourceNick, sourceLogin, sourceHostname, reason);

    User disconnected = connectedUsers.get(sourceNick);
    connectedUsers.remove(sourceNick);
    if(disconnected != null){
      disconnectedUsers.put(sourceNick, disconnected);
    }
  }

  public static void main(String args[])
  {
    PircBot bot = new Cbot();

    bot.setVerbose(true);
    try{
      bot.connect("irc.snoonet.org");
    }catch(IOException e){
      e.printStackTrace();
    }catch(IrcException e){
      e.printStackTrace();
    }

    bot.joinChannel("#cbottest");
  }
  /**
     Determines the type of each element in parameters, and returns them in a Type[] array.
     Any parameters that do not suit any type are assumed Strings.
     Improper parameters will send error messages.
   */
  private Type[] determineParameterTypes(String[] parameters) throws InvalidParameterException
  {
    String error = "";
    Type[] types = new Type[parameters.length];
    for(int i = 0; i < parameters.length; i++){
      String parameter = parameters[i];
      if(parameter.matches("[0-9]*")){
        if(parameter.length() < 9){
          types[0] = Integer.TYPE;
          continue;
        }
        else{
          error += " \'" + parameter + "\' is too big, ";
          continue;
        }
      }
      if(parameter.matches("/true") || parameter.matches("/t")){
        types[i] = Boolean.TYPE;
        continue;
      }
      if(parameter.matches("/false") || parameter.matches("/f")){
        types[i] = Boolean.TYPE;
        continue;
      }
      if(parameter.matches("/u.*")){
        try{
          userFromString(parameter);
        }catch(InvalidParameterException e){
          //User musn't exist. Leave null, add to error and continue to next parameter.
          error += e.getMessage();
          continue;
        }
          types[i] = User.class;
        continue;
      }
      if(parameter.matches("\'.*\'")){
        types[i] = String.class;
        continue;
      }
      if(parameter.matches("\".*\"")){
        types[i] = String.class;
        continue;
      }
      if(connectedUsers.get(parameter) != null){
        types[i] = User.class;
        continue;
      }
      types[i] = String.class;
    }
    if(!error.equals("")){
      throw new InvalidParameterException(error);
    }
    return types;
  }
  /**
     Takes string *input* and splits it into an array separated by spaces(Spaces inside quotes will be preserved).
     *skipSpaces* determines how many spaces it should start in *input*
   * If a quote is left unclosed, an error will be thrown highlighting the unclosed quote.
   */
  private String[] paramterise(String input, int skipSpaces) throws InvalidParameterException{
    ArrayList<String> parameterized = new ArrayList<String>();
    //Skip past *skipSpaces* spaces.
    String skipped;

    String[] split = input.split(" ");
    StringBuilder sb = new StringBuilder(split.length - skipSpaces);
    for(int i = skipSpaces; i < split.length; i++){
      sb.append(split[i]);
    }
    skipped = sb.toString();
    //Find spaces outside of quotes.
    char[] skippedChars = skipped.toCharArray();
    StringBuilder currentParameter = new StringBuilder();
    boolean inQuote = false;
    int quotedAt = 0;
    for(int i = 0; i < skippedChars.length; i++){
      char c = skippedChars[i];
      //Update inQuote status. Ignore if the quote is escaped.
      if((c == '\'' || c == '\"') && skippedChars[i - 1] != '\\'){
        if(!inQuote){
          inQuote = true;
          quotedAt = i;
        }else {
          inQuote = false;
        }
        continue;
      }
      //If we're in a quote, nothing matters except if it's another quote, which is checked above.
      if(inQuote){
        currentParameter.append(c);
        continue;
      }
      //Re: warning: I think readability is better than fancy code.
      if(c == ' ' && !inQuote){
        //Ignore additional spaces
        if(i > 1 && skippedChars[i - 1] == ' '){
          continue;
        }
        parameterized.add(currentParameter.toString());
        //Reset builder
        currentParameter = new StringBuilder();
      }
      currentParameter.append(c);
    }
    //There must be a unfinished quote somewhere.
    if(inQuote){
      highlightError(skippedChars, quotedAt, 4);
    }
    //Convert ArrayList to String[]
    String[] finalOutput = new String[parameterized.size()];
    for(int i = 0; i < parameterized.size(); i++){
      finalOutput[i] = parameterized.get(i);
    }

    return finalOutput;
  }
  private String highlightError(String code, int errorPoint, int contextChars){
    return highlightError(code.toCharArray(), errorPoint, contextChars);
  }

  /**
   * Returns *contextChars* left and right of *code* at *errorPoint* in red with bold center.
   * @param code input text.
   * @param errorPoint The location of the error
   * @param contextChars the amount of characters to reveal left and right.
   * @return the processed text, normally but not always twice the size of contextChars.
   *
   * Special case:
   * If there is not enough room left or right for *contextChars*, it will not be included.
   */
  private String highlightError(char[] code, int errorPoint, int contextChars){
    StringBuilder highlight = new StringBuilder();
    highlight.append(Colors.RED);

    for(int i = Math.max(errorPoint - contextChars, 0); i < Math.min(errorPoint + contextChars, code.length); i++){
      if(i == errorPoint){
        highlight.append(Colors.BOLD);
        highlight.append(code[i]);
        highlight.append(Colors.NORMAL);
        highlight.append(Colors.RED);
        continue;
      }
      highlight.append(code[i]);
      highlight.append(Colors.NORMAL);
    }
    return highlight.toString();
  }

  private User userFromString(String user) throws InvalidParameterException{
    String name = user.replaceAll("/u", "");
    User userObject = connectedUsers.get(name);
    if(userObject == null){
      userObject = disconnectedUsers.get(name);
    }
    if(userObject == null){
      throw new InvalidParameterException(" user " + name + " does not exist,");
    }
    return userObject;
  }

  private Object[] convertTypes(String parameters[]) throws InvalidParameterException{
    Type[] typedObjects;
    Object[] convertedObjects = new Object[parameters.length];
    try{
      typedObjects = determineParameterTypes(parameters);
    }catch(InvalidParameterException e){
      throw e;
    }
    for(int i = 0; i < typedObjects.length; i++)
    {
      Type type = typedObjects[i];
      if(type == String.class){
        convertedObjects[i] = parameters[i];
        continue;
      }
      if(type == User.class){
        convertedObjects[i] = userFromString(parameters[i]);
      }
      if(type == Boolean.TYPE){
        convertedObjects[i] = Boolean.getBoolean(parameters[i]);
      }
      if(type == Integer.TYPE){
        convertedObjects[i] = Integer.parseInt(parameters[i]);
      }
    }
    return convertedObjects;
  }

  public void saveData(Serializable field, String name)
  {
    try{
      FileOutputStream fileOut = new FileOutputStream(name + ".ser");
      ObjectOutputStream out = new ObjectOutputStream(fileOut);
      out.writeObject(field);
      out.close();
      fileOut.close();
    }catch(IOException i){
      i.printStackTrace();
    }
    System.out.println("Saved data to data.ser");
  }

  public  void addCommandListener(Object listener){
    Class<?> klass = listener.getClass();

    while(klass != Object.class){
      List<Method> allMethods = new ArrayList<Method>(Arrays.asList(klass.getDeclaredMethods()));

      for(final Method method : allMethods){
        CommandSettings commandSettings = method.getAnnotation(CommandSettings.class);
        //if listener Annotation isn't null, it must be a instanceof EventSettings.class(AKA annotation)
        if(commandSettings != null){
          commands.put(commandSettings.name(), new Command(method, commandSettings));
        }
      }
      klass = klass.getSuperclass();
    }
  }

  private static class Command{
    private Object[] parameters;
    private Method method;
    private String description;
    private CommandSettings commandSettings;

    private Command(Method method, CommandSettings commandSettings){
      this.method = method;
      this.commandSettings = commandSettings;
    }
  }

}
