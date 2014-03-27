ThinkAlike  
==========
  
### Download ###
Installers and source files can be obtained from [here][release].  
[release]: https://github.com/tiancheng2000/ThinkAlike/releases

What's it?
----------
* It's an open source Java MVVM framework aiming at easier Cross-Platform development among Android and other JVM platforms (e.g.Windows, MacOS).  
* Especially, in case you want to take full advantage of Android native SDK while keeping cross-platform possiblity, this Java solution works.

What it is *NOT*
----------------
* It's not Mono/Xamarin -- .Net solution is nice. But in some cases, Java solution which is Android's native language will be more expected.
* It's not PhoneGap/Cordova -- Hybrid solution still has UI/UX shortcomings to some extent.
* It's not a Productive framework yet. However, it has already verified its feasiblity and extensibility through a probe project and a product-level project.

What's it in detail -- What can it bring to us?
-----------------------------------------------
* Primarily, a ViewModel scheme which can be directly employed to meet basic Activity-level requirements: ICommand and IProprety change notifications.
  The ViewModel then can be reused by Android View implementations and other JVM View implementations (e.g.JavaFX, SWT, Swing). 
  And like MVVM solutions in other background, like WPF, ViewModel brings the convenience of UI-entranced functional testing.  
  *NOTE: To be compatible to Android platform, as you might have considered, JDK 1.6 is preferred in common modules currently.*
* Secondly, author intends to provide & maintain a set of "product-level development foundation" inside of the ThinkAlike.
  Either Android SDK or other Java UI tech such as JavaFX might be new to developers. Well-organized commonly used code will always reduce the learning slope of novice team members.  
  Particularly, author organizes Knowhow nodes in a "cross-platfrom" way as well. There is a "Knowhow Matrix" at back, whose categories looks like .Net Enterprise Library (Config, Log, Exception handling, I/O, Thread etc.), however which is maintained by author personally starting from 2006, through Java/ASP.Net/C#/WPF/Android/HTML5/.. developments.
* Finally, author also *PLAN* to implement View/Control-level ViewModel library so that developers can instantiate generic View/Controls in ViewModel layer (we can only instantiate and customize platform-dependent View/Controls by now).
  Of course, a complete library of Controls (even only support Android and JavaFX platforms) will be a LARGE project. However, in fact, author has already accomplished a HTML parsing & rendering project (with support to layout alignment, attributes, styles. not whole set of HTML/CSS though) by using the MVVM framework. There is a possibility to explore further as long as there is new chance of relevant developments.
  
Where to get more info?
-----------------------
* A [Chinese language blog series][blog] and a [shorter English tech article][article] are available, explaining the usage of ThinkAlike step-by-step. 
  [blog]: http://blog.sina.com.cn/s/articlelist_1926542847_2_1.html
  [article]: http://www.codeproject.com/Articles/749481/How-to-make-your-Android-projects-more-portable-to
* You may contact me @github for details. Generally the following contents will be welcome: 
  * Architectual discussions and suggestions.
  * Pull request! (of course) -- especially if you have had similar idea or project experience.
  * Project consulting. Business, Open Source, new project, legacy projects, just if you'd think like to use ThinkAlike.
  * English usage correction. (I'm a Chinese native speaker working at a Japanese company, so...)

Structural directions
---------------------
* Branches
  * Initial-Base: A beginner's example with all essential framework factors and a simple NodeSelector implemented. 
  * HSCardRef: The main branch by default. Show usage of ThinkAlike by implementing a HearthStone Card Reference.
  * Web-Service: Show how to connect to Web Services within ThinkAlike by implementing an AQI(Air Quality Index) Monitor.

License 
-------
This project is licensed under [The GNU Lesser General Public License (LGPLv3)][LGPLv3].  
[LGPLv3]: http://www.opensource.org/licenses/lgpl-3.0.html

Special Thanks to 
-----------------
* Although this is definitely an architecture-purposed framework, to add some spice to it, some game factors(images) are introduced.  
  [Blizzard][] is a respect-award game entertainment company.  
  [HearthStone][] is its stunning breaker on strategy card game market (under closed beta test phase).  
  ThinkAlike will appear as a simple "Game Card Reference". Commercial license for the card images belongs to [Blizzard][]. Thank them for having created a fantasy World like [WOW][], and furnishing an enjoyable card game table beside the winehouse stove now! 
  [Blizzard]: http://www.blizzard.com/
  [HearthStone]: http://us.battle.net/hearthstone/
  [WOW]: http://us.battle.net/wow/


