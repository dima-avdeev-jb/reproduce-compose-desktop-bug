### Reproduce bug of Compose Desktop
Slack discussion: https://kotlinlang.slack.com/archives/C01D6HTPATV/p1655215430614419  

### How to reproduce:

https://user-images.githubusercontent.com/99798741/174551528-c0c5a77d-b160-4f0a-bd21-19c8c703347a.mov

Press right/left for a bit and you’ll see the value being odd, which it should never be. The last number printed is of course always even. Then move the mouse over the button, which has a mouse-over effect, and this will cause a repaint and the displayed value will be correct again.  

Seems to be a very specific combination of things that causes this. If I do any of these changes, it doesn’t manifest:  
 - Get rid of flatMapLatest
 - Don’t show the generation
 - Remove the delay(1)

### Hardware and environment
Reproduce on MacOS Monterey version 12.4; Intel CPU; Oracle JRE 17, and 11.0.15
![image](https://user-images.githubusercontent.com/99798741/174551957-37257915-ca1b-4567-acdc-579052a3c2db.png)
