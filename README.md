Think Alike
===========

What's it?
----------
It's an open source Java MVVM framework intending to help Cross-Platform development among Android and other JVM platform (e.g.Windows, MacOS).

What's *NOT* it?
----------------
* It's not Mono/Xamarin -- .Net solution is good. But in some cases, Java solution which is Android's Native language will be more than welcome.
* It's not PhoneGap/Cordova -- Hybrid solution still has UI/UX shortcoming to some extent.
* It's not a Productive framework yet. However, it has already verified its feasiblity and extensibility through a probe project and a product-level project.

What's it in detail -- What can it bring to us?
-----------------------------------------------
* Primarily, a ViewModel scheme which can be directly employed to meet basic Activity-level requirements: ICommand and IProprety change notifications.
  The ViewModel then can be reused by Android View implementations and other JVM View implementations (e.g.JavaFX, SWT, Swing). 
  And like MVVM solution in other worlds, like WPF, ViewModel brings the convenience of UI-entranced functional testing.
  *NOTE: To be compatible to Android platform, as you might have considered, JDK 1.6 is preferred in common modules currently.*
* Secondly, author tends to provide & maintenance a set of "product-level development foundation" inside of the ThinkAlike.
  Either Android SDK or other Java UI tech such as JavaFX might be new to developers. Well-organized commonly used code will always decrease the learning slope of novice team members. 
  Author especially organizes Knowhow Nodes in a "cross-platfrom" way as well. There is a "Knowhow Matrix" at back, whose categories looks like .Net Enterprise Library (Config, Log, Exception handling, I/O, Thread etc.), however is maintenanced by author personally starting from 2006, through Java/ASP.Net/C#/WPF/Android/HTML5/.. developments.
* Finally, author also *PLAN* to implement View/Control-level ViewModel library so that developers can instantiate View/Controls in ViewModel module (we need to do this job in platform-dependent View modules currently).
  Off course, a complete set of Controls library (even only support Android and JavaFX platforms) will be of a LARGE project. However, in fact, author has already accomplished a HTML parsing & rendering project (with support to layout alignment, attributes, styles. not full-set of HTML/CSS though) by using the MVVM framework. There is possibility to dig further as long as project requirements raising up.
  
Where to get more info?
-----------------------
* There is a series of blog explaining how to use ThinkAlike framework step-by-step.
  Only a [Chinese language version][blog] is available by now. An English shorter version will be coming soon.
  [blog]: http://blog.sina.com.cn/s/articlelist_1926542847_2_1.html
* You may contact me @github for details. Generally the following contents will be welcome: 
  * Architectual discussions and suggestions.
  * Pull request! (of course) -- especially if you have had similar idea or project experience.
  * Project consulting. Business, Open Source, new project, legacy projects, just if you think of using the ThinkAlike.
  * English usage correction. (I'm a Chinese native speaker working at a Japanese company, so...)

Special Thanks to 
-----------------------
* Although it is definitely an architecture-purposed framework, to put some sugar in it, some game factors(images) are introduced.
  [Blizzard][] is a respect-award game entertainment company. [HealthStone][] is its stunning breaker on strategy card game market (under closed beta test phase). 
  ThinkAlike will appear as a simple "Game Card Reference". Commercial license for the card images belongs to [Blizzard][]. Thank them for having created a fantasy World like [WOW][], and furnishing a suite of funny, elegant card game beside the winehouse stove now. 
  [Blizzard]: http://www.blizzard.com/
  [HealthStone]: http://us.battle.net/hearthstone/
  [WOW]: http://us.battle.net/wow/

