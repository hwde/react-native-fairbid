
Pod::Spec.new do |s|
  s.name         = "RNFairbid"
  s.version      = "0.0.2"
  s.summary      = "RNFairbid"
  s.description  = <<-DESC
                  React Native RNFairbid (wrapper for Fyber Fairbid SDK)
                   DESC
  s.homepage     = "https://github.com/hwde/react-native-fairbid"
  s.license      = "MIT"
  s.author       = { "author" => "heiko@wecos.de" }
  s.platform     = :ios, "9.0"
  s.source       = { :git => "https://github.com/hwde/react-native-fairbid.git", :tag => "master" }
  s.source_files  = "ios/**/*.{h,m}"
  s.requires_arc = true

  s.dependency "React"
  s.dependency "FairBidSDK"

end

