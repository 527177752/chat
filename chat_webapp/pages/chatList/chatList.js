const app = getApp();
const util = require('../../utils/util.js')
Page({
  data: {
    myId: 2,
  },

  goChat(e) {
    wx.navigateTo({
      url: '/pages/chat/chat?id='+e.currentTarget.dataset.id + '&name=' + e.currentTarget.dataset.name,
    })
  },

  onLoad() {
    this.getUnread()
  },

  onShow: function () {
    //获取和张三的聊天记录（简单写死）
    let content = wx.getStorageSync(`${this.data.myId}to1`) || []
    let count = wx.getStorageSync(`chat${this.data.myId}to1`) || 0
    if (content.length > 0) {
      this.setData({
        [`chat${this.data.myId}to1Time`]: content[(content.length-1)].time.substring(11,19),
        [`chat${this.data.myId}to1Content`]: content[(content.length-1)].content,
        chat2to1: count
      })
    }

    //获取和王五的聊天记录（简单写死）
    let content2 = wx.getStorageSync(`${this.data.myId}to3`) || []
    let count2 = wx.getStorageSync(`chat${this.data.myId}to3`) || 0
    if (content2.length > 0) {
      this.setData({
        [`chat${this.data.myId}to3Time`]: content2[(content2.length-1)].time.substring(11,19),
        [`chat${this.data.myId}to3Content`]: content2[(content2.length-1)].content,
        chat2to3: count2,
      })
    }

    let that=this;
    wx.onSocketOpen(function (res) {
    })
 
    //将获取到的聊天信息存入本地缓存
    wx.onSocketMessage(function (data) {
      console.log("onSocketMessage ", data)
      let message = JSON.parse(data.data)
      that.savaChatToLocal(message)
    })
  },

  /**
   * 将获取到的聊天信息存入本地缓存
   * @param {*} message 
   */
  savaChatToLocal(message) {
      let that = this
      message.time =  util.formatTime(new Date(message.createTime))
      let array = wx.getStorageSync(`${that.data.myId}to${message.sendId}`) || []
      array.push(message)
      wx.setStorageSync(`${that.data.myId}to${message.sendId}`,array) 
      if (that.data[`chat${that.data.myId}to${message.sendId}`]) {
        that.setData({
          [`chat${that.data.myId}to${message.sendId}`]: that.data[`chat${that.data.myId}to${message.sendId}`] + 1
        })
      } else {
        that.setData({
          [`chat${that.data.myId}to${message.sendId}`]: 1
        })
      }
      that.setData({
        [`chat${that.data.myId}to${message.sendId}Time`]: message.time.substring(11,19),
        [`chat${that.data.myId}to${message.sendId}Content`]: message.content
      })
      wx.setStorageSync(`chat${that.data.myId}to${message.sendId}`,that.data[`chat${that.data.myId}to${message.sendId}`])
  },

  getUnread() {
    let that = this
    wx.request({
      url: 'http://localhost:18092/chat/unread/getUnread.do', 
      data: {id: this.data.myId},
      header: {
        'content-type': 'application/json' // 默认值
      },
      success (res) {
        console.log(res.data)
        for(let i of res.data) {
          that.savaChatToLocal(i)
        }
      },
      fail (res) {
        console.log(res.data)
      },
    })
  }


})