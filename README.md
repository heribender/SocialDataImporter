SocialDataImporter
==================

Tool for bulk import of users into social platforms.

Project leader: Heri Bender, Nena1, Zürich, Switzerland (heribender@web.de)
Start date: 05.11.2014
Technologies:
  - java 8
  - spring context
  - maven

Web masters of social platform often have the need to import a bulk of new users. Unfortunatly many platforms do not offer a convenient way to accomplish this.

The main tasks of doing such an import is very similar independant from platform to platform, although some details in collecting the data and deliver them in a suitable format to a particular platform may differ slightly.

I am in a situation where I need such a tool for importing several hundred new users to a closed user group (our group, the housing cooperative Nena1, runs an oxwall instance on a private web server). I am a professinal java programmer who likes to play with new technologies (this can hardly be done in my daily professional life where we have to maintain legacy applications). Instead of doing a quick and dirty hack only for my needs I decided to roll out a flexible tool based on the newest java and spring technologies.

The tool will consist of following main modules:

- Input data collector
- Input data transformer
- Target importer
- User notification

Feel free to join! I am looking forward discussing architectural decisions.

