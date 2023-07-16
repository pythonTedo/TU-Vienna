##################################################################################
# Basis Expansions in R
##################################################################################

# Example weight loss:

library(MASS)
data(wtloss)
plot(Weight~Days, data=wtloss)

# Fit a linear model:
lm1 <- lm(Weight~Days, data=wtloss)
abline(lm1, col="blue")

# Fit a linear model with quadratic term
lm2 <- lm(Weight~Days+I(Days^2), data=wtloss)
lines(wtloss$Days, predict.lm(lm2), col="green")
legend("topleft", legend=c("linear", "quadratic"), col  = c("blue", "green"), lty=c(1,1))

# Extrapolation:
plot(Weight~Days, data=wtloss, xlim=c(0,1000), ylim=c(0,400))
abline(lm1, col="blue")
x <- c(wtloss$Days, seq(300,1000,length=50))
lines(x,lm2$coef[1]+x*lm2$coef[2]+x^2*lm2$coef[3], col="green")
legend("topleft", legend=c("linear", "quadratic"), col  = c("blue", "green"), lty=c(1,1))



# Nonlinear regression with nls:

mod.start <- c(b0=100, b1=85, theta=100)
mod.nls <- nls(Weight~b0+b1*2^(-Days/theta), data=wtloss, 
               start=mod.start, trace=TRUE)

plot(Weight~Days, data=wtloss, xlim=c(0,1000), ylim=c(0,400))
abline(lm1, col="blue")
x <- c(wtloss$Days, seq(300,1000,length=50))
lines(x,lm2$coef[1]+x*lm2$coef[2]+x^2*lm2$coef[3], col="green")
lines(x, predict(mod.nls, list(Days=x)), col="orange")
abline(h=81.37 , lty=3, col = "red")
legend("topleft", legend=c("linear", "quadratic", "nls"), 
       col=c("blue", "green", "orange"), lty=c(1,1,1))



# General optimization with optim

funSSR<-function(p){
  sum((wtloss$Weight - (p[1] + p[2] * 2^(-wtloss$Days/p[3])))^2)
}
mod.opt1 <- optim(mod.start, funSSR)
mod.opt1

funABSR<-function(p){
  sum(abs((wtloss$Weight - (p[1] + p[2] * 2^(-wtloss$Days/p[3])))))
}
mod.opt2 <- optim(mod.start, funABSR)
mod.opt2

plot(Weight~Days, data=wtloss, xlim=c(0,1000), ylim=c(0,400))
abline(lm1, col="blue")
x <- c(wtloss$Days, seq(300,1000,length=50))
lines(x,lm2$coef[1]+x*lm2$coef[2]+x^2*lm2$coef[3], col="green")
lines(x, predict(mod.nls, list(Days=x)), col="orange")
abline(h=81.37 , lty=3, col = "red")
lines(x, mod.opt1$par[1]+mod.opt1$par[2]*2^(-x/mod.opt1$par[3]), col="violetred", lty=2)
lines(x, mod.opt2$par[1]+mod.opt2$par[2]*2^(-x/mod.opt2$par[3]), col="cyan")
legend("topleft", legend=c("linear", "quadratic", "nls", "optimSSR", 
      "optimABSR"), col=c("blue", "green", "orange", "violetred", "cyan"), lty=c(1,1,1,2,1))


summary(mod.nls)
summary(mod.opt1)



##############################################################################
# Interpolation with splines:
##############################################################################
#
# Construct splines from lecture notes
lecturespl <- function(x,nknots=2,M=4){
  # nknots ... number of knots -> placed at regular quantiles
  # M ... M-1 is the degree of the polynomial
  n <- length(x)
  # X will not get an intercept column
  X <- matrix(NA,nrow=n,ncol=(M-1)+nknots)
  for (i in 1:(M-1)){
    X[,i] <- x^i
  }
  # now the basis functions for the constraints
  quant <- seq(0,1,1/(nknots+1))[c(2:(nknots+1))]
  qu <- quantile(x,quant)
  for (i in M:(M+nknots-1)){
    X[,i] <- ifelse(x-qu[i-M+1]<0,0,(x-qu[i-M+1])^(M-1))
  }
  list(X=X,quantiles=quant,xquantiles=qu,x=x)
}
plotspl <- function(splobj,...){
  matplot(splobj$x,splobj$X,type="l",lty=1,
          xlab="x",ylab="h(x)",...)
  abline(v=splobj$xquantiles,lty=3,col=gray(0.5))
}
##################################################################################
# Demo example:

# fit simulated sine function
set.seed(1234)
x <- seq(1,10,length=100)
y <- sin(x) + 0.1 * rnorm(x)
x1 <- seq(-1,12,length=100)
plot(x, y, xlim = range(x1))

spl <- lecturespl(x,nknots=2,M=4)
lm1 <-  lm(y ~ spl$X)
lines(x,predict(lm1,newdata=data.frame(x)), col="blue")

plotspl(spl)
plotspl(spl,ylim=c(0,20))

plot(x, y, xlim = range(x1))
spl <- lecturespl(x,nknots=6,M=4)
lm2 <-  lm(y ~ spl$X)
lines(x,predict(lm2,newdata=data.frame(x)), col="green")

plotspl(spl)



# Model fit with B-Splines

library(splines)
matplot(x,bs(x, knots=5, degree = 2), type="l",lty=1)
matplot(x,bs(x, df = 4, degree=3), type="l",lty=1)
matplot(x,bs(x, knots=c(3,7), degree=3), type="l",lty=1)

# Estimation of the parameters
set.seed(1234)
x <-  seq(1,10,length=100)
y <-  sin(x) + 0.1 * rnorm(x)
x1 <- seq(-1,12,length=100)
plot(x, y, xlim = range(x1))

lm1 <- lm(y ~ bs(x, df=4))
lines(x1,predict.lm(lm1,list(x=x1)), col="blue")


# Increase df:
lm2 <- lm(y ~ bs(x, df=6))
lines(x1,predict.lm(lm2,list(x=x1)), col="green")
legend("topright",legend=c("df=4","df=6"),col=c("blue","green"),lty=c(1,1))



# Using natural cubic splines:
lm3N <- lm(y ~ ns(x, df=6))
plot(x, y, xlim = range(x1))
lines(x1,predict.lm(lm3N,list(x=x1)), col="orange")

matplot(x,ns(x, df=6), type="l", lty=1)


# Smoothing splines:
m1 <- smooth.spline(x, y, df=6)
plot(x, y, xlim = range(x1), ylim=c(-1.5, 1.5))
lines(m1, col="green")


# Prediction:
plot(x, y, xlim = range(x1), ylim=c(-1.5, 1.5))
lines(predict(m1, x1), col="blue")


# Choice of df with CV
set.seed(123)
m2 <- smooth.spline(x, y, cv=TRUE)
plot(x, y, xlim = range(x1), ylim=c(-1.5, 1.5))
lines(predict(m1, x1), col="blue")
lines(predict(m2, x1), col="green")
legend("bottomleft",legend=c("df=6", paste("df=",round(m2$df,1),sep="")),lty=c(1,1),col=c("blue","green"))


###########################################################################################
# Use ns for multiple linear regression:
# Body fat data:

library("UsingR")
data(fat)
fat <- fat[-c(31,39,42,86), -c(1,3,4,9)]# strange values, not use all variables
attach(fat)

# randomly split into training and test data:
set.seed(123)
n <- nrow(fat)
train <- sample(1:n,round(n*2/3))
test <- (1:n)[-train]

# linear model: abdomen, hip, wrist are important
model.lm <- lm(body.fat~., data = fat, subset=train)
summary(model.lm)
pred.lm <- predict(model.lm,newdata = fat[test,])
plot(fat[test,"body.fat"],pred.lm,xlab="y measured",ylab="y predicted",cex.lab=1.3)
abline(c(0,1))

mod <- lm(body.fat~ns(abdomen,4)+ns(hip,4)+ns(wrist,4), data=fat)
summary(mod)
pred <- predict(mod,fat[test,])
plot(fat[test,"body.fat"],pred,xlab="y measured",ylab="y predicted",cex.lab=1.3)
abline(c(0,1))

X <- model.matrix(mod)
plot(fat$abdomen,X[,2:5]%*%mod$coefficients[2:5])


# Smoothing Splines
require(graphics)
plot(dist ~ speed, data = cars)
cars.spl <- smooth.spline(cars$speed, cars$dist,cv=FALSE) # is doing GCV
cars.spl
## This example has duplicate points, so avoid cv = TRUE

lines(cars.spl, col = "blue")
ss10 <- smooth.spline(cars$speed, cars$dist, df=10)
lines(ss10, lty = 2, col = "red")
legend(5,120,c(paste("default [C.V.] => df =",round(cars.spl$df,1)),
               "s( * , df = 10)"), col = c("blue","red"), lty = 1:2,
       bg = 'bisque')




# Smoothing Splines with the bone density data:
library(ElemStatLearn)
data(bone)

plot(spnbmd ~ age, data=bone, col = 
     ifelse(gender=="male", "blue", "red2"), 
     xlab="Age", ylab="Relative Change in Spinal BMD")
bone.spline.male <- with(subset(bone,gender=="male"),
                         smooth.spline(age, spnbmd,cv=FALSE))
bone.spline.female <- with(subset(bone, gender=="female"), 
                smooth.spline(age, spnbmd, cv=FALSE))
lines(bone.spline.male, col="blue")
lines(bone.spline.female, col="red2")
legend("topright", legend=c("Male", "Female"), col=c("blue", "red2"), lwd=2)






