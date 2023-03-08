# ShadowDimension-game

This game invloves 2 stages.

# 1st stage: 
player has to reach the final gate avoiding sinkholes(that inflicts damage). Walls are stationary entities that donot causes any damage.

# 2nd stage:
Player has to defeat the monster enemy Navec to win. There are several enemies in this stage which includes moving demons, stationary demons. Demons shoots fire when player gets closer to them. Player can kill the demons as well. Either the demons are stationary or moving depends on the randomness method implemented in the program. Like satge 1 there are some staionary objects: trees and sinkhole. Sinkhole can cause damage to the player. Demon and Navec can shoot fire in 4 directions NW, SW, NE, SW depending on the location of player within thier attacking circumference.
Games end if player looses all energy in stage 1 incurring damges from sinkhole. If player is able to reach to stage 2, game can end when player's energy goes to 0 either by inflicting damage from Navec, Demons, sinkholes.

We have also added some extra featues into game:
Timescale speed can be varried. This is to increase or decrease the difficulty level of stage 2.
By pressing "L" key speed of enemies increases and can reach to maximum of +3
By pressing "K" key speed of enemies decreases can reach to minimum of -3
Note that intitially timescale settings are set to 0. (speed is selected randomly by program)

Player can attack enemies with "A" key. Also, once a player gets attacked it goes to invincible state for 3000 miliseconds in which attack on the player will not cause any damage. When "A" key is pressed player can remain in Attacking state for 1000 miliseconds in which they can inflict damage on enemies. This means that they return to IDLE state after this time period, then there is a cooldown of 2000 miliseconds. After this player can attack again.

Also, if demon recieve damage from player they go into invincible state for 3000 miliseconds. In this state any attack caused by player won't cause any damage to demon. Likewise, Navec also gets into invincible state when they recieve damage from player.

Note that for clearity the pictures used for Demon and Navec in invincible and normal state will be different during game. Player also have different images for different states.

Player can inflict damage points of 20. 
Demon can inflict damage of 10. Demons attack range is 150 pixels
Navec can inflict damage of 20. Navec's attack range is 200 pixels
Sinkhole can inflict damage points of 30 on player.

