'use strict';

angular.module('mutrack')
  .controller('MainCtrl', function ($scope, $modal, $log, PackageSrv, SchedulerTrackSrv, localStorageService, ngNotify) {
    // Local storage of public packages.
    var packagesInStore = localStorageService.get('packages');

    $scope.packages = packagesInStore || [];

    $scope.$watch('packages', function() {
      localStorageService.set('packages', $scope.packages);
    }, true);

    $scope.savePackage = function(pack) {
      pack.code = pack.code.toUpperCase();
      $scope.packages.push(pack);
      ngNotify.set('Pacote \'' + pack.code + '\' salvo, buscando o último status!', 'success');
      PackageSrv.trackLastEvent(pack);
    };

    $scope.deletePackage = function(pack) {
      var indexOf = $scope.packages.indexOf(pack);
      $scope.packages.splice(indexOf, 1);
    };

    // Open Modal (Add a New Package)
    $scope.openAddPackage = function () {
      var modalInstance = $modal.open({
        animation: true,
        templateUrl: 'packageModalContent.html',
        controller: 'PackageModalCtrl',
        size: 'lg',
        resolve: {
          pack: function () {
          }
        }
      });

      modalInstance.result.then(function (pack) {
        $scope.savePackage(pack);
      });
    };

    // Open Modal (Full Track of a Package)
    $scope.openTrackPackage = function (code) {
      var modalInstance = $modal.open({
        animation: true,
        templateUrl: 'trackModalContent.html',
        controller: 'TrackPackageModalCtrl',
        size: 'lg',
        resolve: {
          code: function () {
            return code;
          }
        }
      });

      modalInstance.result.then(function () { });
    };

    // Track the last event of all packages.
    PackageSrv.trackMultipleLastEvent(PackageSrv.selectPackagesToTrack($scope.packages));

    // Start a scheduler to track packages.
    SchedulerTrackSrv.track($scope.packages);
});
