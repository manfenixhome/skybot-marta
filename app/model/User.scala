package model

/**
  * Created by cheb on 7/9/16.
  */
class User(
            id: Long,
            about: String,
            name: String,
            address: String,
            birthday: String,
            closePersonPhoneNumber: String,
            hobby: String,
            homeEmail: String,
            workingEmail: String,
            loginToComputer: String,
            phoneNumber: String,
            title: String,
            skype: String,
            startWorking: String,
            securityKeyToComputer: String,
            images: Seq[Image],
            technology: Seq[Technology],
            caste: Seq[Caste],
            language: Seq[Language]
          ) {

}
