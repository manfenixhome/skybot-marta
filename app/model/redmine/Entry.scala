package model.redmine

/**
  * Created by cheb on 7/10/16.
  */
class Entry(
             id: Long,
             project: Project,
             issue: Issue,
             user: RedmineUser,
             activity: Activity,
             hours: Double,
             comments: String,
             spent_on: String
           ) {

}
