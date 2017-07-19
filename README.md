# Outreach-API-LogicMonitor-Datasource
A LogicMonitor datasource written in GROOVY to monitor internal BDR performance by querying the Outreach API.

The datasource consists of two scripts.  The first script actively discovers unlocked Outreach users by user ID.
The second script queries the Outreach API for calls made daily by each user, and requests information about each call.

The datasource monitors:
  How many calls were made and when
  How many calls were answered and when
  How many voicemails were left and when
  How many "ghost calls" were made and when
  What percentage of calls have been answered
