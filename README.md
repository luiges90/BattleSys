#BattleSys - Battle System 
###for battling forum game "第N次戰鬥系列"

** NOTE: This project is no longer maintained. ** 

Output sample: http://hksan.net/forum/index.php?showtopic=12813

To enable image uploading feature, you need to set up FTP hosts, username and password. Copy and rename ftp_sample.txt to ftp.txt and place the details as in the file. 


Note that the host need to have `battleSysImage/results/<gametime><hf>` folder in order for image uploading feature to work correctly, where `<gametime>` is round time and `<hf>` is whether it is heat/final, both can be changed in `TournamentPoints.txt`

Netbeans project is also included for ease of opening and compiling the project with Netbeans. If you get Netbeans complaining reference problems about "tools.jar", fix it by pointing it to simpleftp.jar.

## Game rules

TODO

## Usage

- To create Player Files, use PlayerInputterForm to create a sctatch. Additional features can be done by editing the created *.java file. Note that players bearing the same team name will be in the same team in tournaments.
- To adjust Tournament settings, edit tournamentPoints.txt and read the description in it for instruction (current only in Chinese.)
- To adjust Random Events settings, edit RandomEvents.csv and read the description in it for instruction (current only in Chinese.)
- To start a battle and generate the results, run the _battle runner_ NthBattleForm.java. The result will be in RESULTS/name, where name is a concatination of battle round number and heat/final value (both can be set in tournamentPoints.txt)
- Advanced: You can edit the generated player files to do more things.
- `the constructor` sets up its name `Player::setName(name)`, team name `Player::setTeamName(name)`, points `Player:giveHp/Atk/Def/Spd/MorPoint(point)` and moves `buyAtk(moveClassName)` it can use. Note that if this setting exceeds number of point allowed (as set in tournamentPoints.txt), the file will get rejected by the battle runner.
- You may also use `setAtkName("MoveClassName", "aliasname")` and `setCritName("MoveClassName", "aliasname")` to set unique attack names.
- `move` method instruct the player how to move. You can put actual java code here to make the player's movement more varied.
  - `useMove("MoveClassName", thisPlayers, opposingPlayers)` uses a move
  - You may want to use `getMoveTime("MoveClassName")` to check if there are any remaining moves it can use before actually using it.
  - You may also want to access each player's abilities or run anything on Players to do the decision. Read JavaDoc for details.
- Last but not least, you may want to override `preBattleSpeech, postBattleSpeech, deadSpeech` to make your player say something. To "say something" use `speak("message")`

## Credits

This project makes use of the following open-source libraries:    
JavaCSV - http://sourceforge.net/projects/javacsv/    
SimpleFTP - http://www.jibble.org/simpleftp/    
Mersenne Twister Random Number Generator in Java - http://www.cs.gmu.edu/~sean/research/
