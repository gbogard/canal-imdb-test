package com.canal.domain.errors

class LimitTooHigh(maxLimit: Int) extends RuntimeException(s"Limit cannot exceed $maxLimit")