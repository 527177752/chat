const app = getApp();
const util = require('../../utils/util.js')
Page({
  data: {
    scrollWithAnimation: false,
    myId:2,
    sendId: 2,
    recId: 1,
    InputBottom: 0,
    sendMsg: '',
    scrollTop: 0,
    high: 0,
    list: [],
  },
  onShow: function () {
    var that=this;
    wx.setStorageSync(`chat${that.data.sendId}to${that.data.recId}`,0)
    wx.onSocketOpen(function (res) {
    })
 
    wx.onSocketMessage(function (data) {
      console.log("onSocketMessage ", data)
      let message = JSON.parse(data.data)
      message.time =  util.formatTime(new Date(message.createTime))
      // 接收这个人发送给我的消息
      if (that.data.recId == message.sendId) {
        let array = wx.getStorageSync(`${that.data.myId}to${message.sendId}`) || []
        array.push(message)
        wx.setStorageSync(`${that.data.myId}to${message.sendId}`,array) 
        wx.setStorageSync(`chat${that.data.myId}to${that.data.sendId}`,0)
        that.setData({              
          list: wx.getStorageSync(`${that.data.sendId}to${that.data.recId}`) ,
        })
        that.setData({              
          scrollTop: 999999999999999
        })
      }

      // 接收自己发送的消息
      else if ( message.sendId == that.data.sendId && message.recId == that.data.recId ) {
        let array = wx.getStorageSync(`${that.data.myId}to${message.recId}`) || []
        array.push(message)
        wx.setStorageSync(`${that.data.myId}to${message.recId}`,array) 
        wx.setStorageSync(`chat${that.data.myId}to${that.data.recId}`,0)
        that.setData({              
          list: wx.getStorageSync(`${that.data.sendId}to${that.data.recId}`) ,
        })
        that.setData({              
          scrollTop: 999999999999999
        })
      }

      // 接收其他人发送给我的消息
      else {
        let array = wx.getStorageSync(`${that.data.myId}to${message.sendId}`) || []
        array.push(message)
        wx.setStorageSync(`${that.data.myId}to${message.sendId}`,array) 
        let count = wx.getStorageSync(`chat${that.data.myId}to${message.sendId}`) || 0
        wx.setStorageSync(`chat${that.data.myId}to${message.sendId}`,count + 1) 
      }
    })
  },
  onLoad(e) {
    this.setData({
      recId: Number(e.id)
    })
    wx.setNavigationBarTitle({
      title: e.name,
    })
    let content =  wx.getStorageSync(`${this.data.sendId}to${this.data.recId}`) || []
    this.setData({
      list: content,
      scrollTop: 999999999999999,
    })
    this.setData({
      scrollWithAnimation: true
    })

  },

  send() {
    let that = this
    wx.sendSocketMessage({
      data: JSON.stringify({recId: this.data.recId, message: this.data.sendMsg}),
      success: function (res) {
        that.setData({
          sendMsg: ''
        })
        console.log("sendSocketMessage 成功")
      },
      fail: function (res) {
        console.log("sendSocketMessage 失败")
        wx.showToast({
          title: '发送消息失败',
          icon: 'none'
        })
      }
    });      
  },

  InputFocus(e) {
    this.setData({
      InputBottom: e.detail.height,
      high: e.detail.height,
    })
    this.setData({
      scrollTop: 99999999999
    })
  },
  bindinput(e) {
    console.log(e.detail.value)
    this.setData({
      sendMsg: e.detail.value
    })
  },
  InputBlur(e) {
    this.setData({
      InputBottom: 0,
      high:0
    })
  },
})